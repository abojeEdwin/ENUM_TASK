package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.TalentStatus;
import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.data.model.VerificationToken;
import com.EnumDayTask.data.repositories.TalentRepo;
import com.EnumDayTask.data.repositories.VerificationTokenRepo;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.exception.*;
import com.EnumDayTask.util.AppUtils;
import com.EnumDayTask.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static com.EnumDayTask.util.AppUtils.*;


@Service
public class TalentAuthImpl implements TalentAuthService{

    @Autowired
    private TalentRepo talentRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private VerificationTokenRepo verificationTokenRepository;


    @Override
    public CreateAccountRes signup(CreateAccountReq createAccountReq) throws EMAIL_IN_USE{
        Optional<Talent> foundTalent = talentRepo.findByEmail(createAccountReq.getEmail());
        if (foundTalent.isPresent()) {
            Talent talent = foundTalent.get();
            switch (talent.getStatus()) {
                case VERIFIED -> throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);
                case PENDING_VERIFICATION -> {
                    String token = jwtUtils.generateToken(talent);
                    CreateAccountRes response = new CreateAccountRes();
                    response.setToken(token);
                    response.setMessage(VERIFICATION_RESENT);
                    return response;
                }
            }
        }
        String hashedPassword = AppUtils.hashPassword(createAccountReq.getPassword());

        VerificationToken verificationToken = new VerificationToken();

        Talent talent = new Talent();
        talent.setEmail(createAccountReq.getEmail());
        talent.setPassword(hashedPassword);
        talent.setStatus(TalentStatus.PENDING_VERIFICATION);
        Talent savedTalent = talentRepo.save(talent);
        CreateAccountRes response = new CreateAccountRes();

        String token = jwtUtils.generateToken(talent);
        verificationToken.setToken(token);
        verificationToken.setUserEmail(savedTalent.getEmail());
        verificationToken.setUsed(false);
        verificationToken.setExpiryDate(jwtUtils.extractExpiration(token));
        verificationTokenRepository.save(verificationToken);

        response.setToken(token);
        response.setMessage(USER_CREATED);
        return response;
    }

    @Override
    public Talent verifyEmail(String token) {
        boolean isExpired = jwtUtils.isTokenExpired(token);
        if(isExpired){throw new TOKEN_EXPIRED(TOKEN_ALREADY_EXPIRED);}
        String email = jwtUtils.extractEmail(token);
        VerificationToken storedToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TOKEN_INVALID(TOKEN_INVALID));

        if (storedToken.isUsed()) {throw new TOKEN_ALREADY_USED(TOKEN_ALREADY_USED);}

          storedToken.setUsed(true);
            verificationTokenRepository.save(storedToken);

            Optional<Talent> foundTalent = Optional.ofNullable(talentRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(AppUtils.USER_NOT_FOUND)));
            if (foundTalent.isPresent()) {
                Talent talent = foundTalent.get();
                talent.setStatus(TalentStatus.VERIFIED);
                return talentRepo.save(talent);
            } else {
                throw new UserNotFoundException(AppUtils.USER_NOT_FOUND);
            }
        }

    @Override
    public void deleteAll() {
        talentRepo.deleteAll();
    }






}

