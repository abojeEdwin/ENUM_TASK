package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.TalentStatus;
import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.exception.EMAIL_IN_USE;
import com.EnumDayTask.exception.TOKEN_EXPIRED;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TalentAuthImplTest {

    @Autowired
    private TalentAuthImpl talentService;


    @BeforeEach
    public void setUp(){
        talentService.deleteAll();
    }
    @AfterEach
    public void tearDown(){
        talentService.deleteAll();
    }

    @Test
    public void testUserCreateAccountWithValidEmailAndPassword(){
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);
    }

    @Test
    public void testUserCreateAccountWithValidEmailAndVerifiesEmail(){
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertNotNull(verifiedTalent);
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());
        assertEquals(request.getEmail(), verifiedTalent.getEmail());

    }

    @Test
    public void testUserTriesToCreateAccountWithSameValidEmailAndIsVerified(){
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);


        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertNotNull(verifiedTalent);
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());
        assertEquals(request.getEmail(), verifiedTalent.getEmail());


        CreateAccountReq request2 = new CreateAccountReq();
        request2.setEmail("abojeedwin@gmail.com");
        request2.setPassword("SecurePassword123!");
        assertThrows(EMAIL_IN_USE.class, () -> talentService.signup(request2));
    }

    @Test
    public void testUserAlreadyExistButNotVerifiedAndTriesToSignupAgain() {
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        CreateAccountReq request2 = new CreateAccountReq();
        request2.setEmail("abojeedwin@gmail.com");
        request2.setPassword("SecurePassword123!");
        CreateAccountRes response2 = talentService.signup(request);
        assertNotNull(response2);

        assertEquals(response2.getMessage(), "Verification email sent successfully");
    }

    @Test
    public void testUserTriesToVerifyEmailWithExpiredToken(){
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        String expiredToken = Jwts.builder()
                .setSubject("abojeedwin@gmail.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
                .setExpiration(new Date(System.currentTimeMillis() - 50000))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode("Ge2vmjIPvv2VoV2nGjMWnMEKhcfp51xuElyf60oVM9R2Mbci1uHY39Ddwh7BK2PlYSPj2k7fPqCXJGIM//8tOQ==")), SignatureAlgorithm.HS256)
                .compact();
        assertNotNull(expiredToken);

        assertThrows(TOKEN_EXPIRED.class,()->talentService.verifyEmail(expiredToken));
    }

}