package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.TalentStatus;
import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.data.model.VerificationToken;
import com.EnumDayTask.data.repositories.TalentRepo;
import com.EnumDayTask.data.repositories.VerificationTokenRepo;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Request.LoginTalentReq;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.dto.Response.LoginTalentRes;
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
            if (talent.getStatus() == TalentStatus.VERIFIED) {
                throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);
            } else {
                String token = jwtUtils.generateToken(talent);
                VerificationToken verificationToken = new VerificationToken();
                verificationToken.setToken(token);
                verificationToken.setUserEmail(talent.getEmail());
                verificationToken.setUsed(false);
                verificationToken.setExpiryDate(jwtUtils.extractExpiration(token));
                verificationTokenRepository.save(verificationToken);

                CreateAccountRes response = new CreateAccountRes();
                response.setToken(token);
                response.setMessage(VERIFICATION_RESENT);
                return response;
            }
        } else {
            String hashedPassword = AppUtils.hashPassword(createAccountReq.getPassword());

            Talent talent = new Talent();
            talent.setEmail(createAccountReq.getEmail());
            talent.setPassword(hashedPassword);
            talent.setStatus(TalentStatus.PENDING_VERIFICATION);
            Talent savedTalent = talentRepo.save(talent);

            String token = jwtUtils.generateToken(savedTalent);
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUserEmail(savedTalent.getEmail());
            verificationToken.setUsed(false);
            verificationToken.setExpiryDate(jwtUtils.extractExpiration(token));
            verificationTokenRepository.save(verificationToken);

            CreateAccountRes response = new CreateAccountRes();
            response.setToken(token);
            response.setMessage(USER_CREATED);
            return response;
        }
    }

    @Override
    public Talent verifyEmail(String token) {
        boolean isExpired = jwtUtils.isTokenExpired(token);
        if(isExpired){throw new TOKEN_EXPIRED(TOKEN_ALREADY_EXPIRED);}

        String email = jwtUtils.extractEmail(token);
        VerificationToken storedToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TOKEN_INVALID(TOKEN_INVALID));
        if (storedToken.isUsed()) {throw new TOKEN_ALREADY_USED(TOKEN_ALREADY_USED);}

        Talent talent = talentRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(AppUtils.USER_NOT_FOUND));
        talent.setStatus(TalentStatus.VERIFIED);
        storedToken.setUsed(true);
        verificationTokenRepository.save(storedToken);
        return talentRepo.save(talent);
    }

    @Override
    public LoginTalentRes login(LoginTalentReq request) {
        Optional<Talent> foundTalent = talentRepo.findByEmail(request.getEmail());
        if (foundTalent.isPresent()) {
            Talent talent = foundTalent.get();
            if (talent.getStatus() == TalentStatus.VERIFIED) {
                AppUtils.verifyPassword(request.getPassword(), talent.getPassword());
                    String token = jwtUtils.generateToken(talent);
                    LoginTalentRes response = new LoginTalentRes();
                    response.setToken(token);
                    response.setMessage(LOGIN_SUCCESSFUL);
                return response;
            }
        }
        return null;
    }

    @Override
    public void deleteAll() {
        talentRepo.deleteAll();
        verificationTokenRepository.deleteAll();
    }
}
