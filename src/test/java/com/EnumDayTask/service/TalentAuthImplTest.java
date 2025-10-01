package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.TalentStatus;
import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Request.LoginTalentReq;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.dto.Response.LoginTalentRes;
import com.EnumDayTask.exception.*;
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

        assertEquals("Verification email sent successfully", response2.getMessage());
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

    @Test
    public void testUserLoginWithValidCredentials() {
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertNotNull(verifiedTalent);
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());
        assertEquals(request.getEmail(), verifiedTalent.getEmail());

        LoginTalentReq req = new LoginTalentReq();
        req.setEmail("abojeedwin@gmail.com");
        req.setPassword("SecurePassword123!");
        LoginTalentRes res = talentService.login(req);
        assertNotNull(res);
        assertNotNull(res.getToken());
        assertEquals("Login successful", res.getMessage());

    }

    @Test
    public void testUserLoginWhenEmailNotVerified(){
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        LoginTalentReq req = new LoginTalentReq();
        req.setEmail("abojeedwin@gmail.com");
        req.setPassword("SecurePassword123!");
        assertThrows(EMAIL_NOT_VERIFIED.class,()->talentService.login(req));
    }

    @Test
    public void testUserLoginWithInvalidCredentials(){
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertNotNull(verifiedTalent);
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());
        assertEquals(request.getEmail(), verifiedTalent.getEmail());

        LoginTalentReq req = new LoginTalentReq();
        req.setEmail("abojeedwin@gmail.com");
        req.setPassword("wrongpassword");
        assertThrows(INVALID_CREDENTIAL.class,()->talentService.login(req));

    }

    @Test
    public void testRateLimitIsTriggeredAfterMultipleFailedLogins() {
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        assertNotNull(response);

        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertNotNull(verifiedTalent);
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());
        assertEquals(request.getEmail(), verifiedTalent.getEmail());

        LoginTalentReq req = new LoginTalentReq();
        req.setEmail("abojeedwin@gmail.com");
        req.setPassword("wrongpassword");

        for (int i = 0; i < 5; i++) {
            assertThrows(INVALID_CREDENTIAL.class, () -> talentService.login(req));
        }

        assertThrows(RATE_LIMITED.class, () -> talentService.login(req));
    }

    @Test
    public void testUserLogsOutSuccessfully() {
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());

        LoginTalentReq req = new LoginTalentReq();
        req.setEmail("abojeedwin@gmail.com");
        req.setPassword("SecurePassword123!");
        LoginTalentRes res = talentService.login(req);
        String token = res.getToken();
        assertNotNull(token);

        talentService.logout(token);

        assertThrows(TOKEN_INVALID.class, () -> talentService.verifyEmail(token));
    }

    @Test
    public void testLogoutIsIdempotent() {
        CreateAccountReq request = new CreateAccountReq();
        request.setEmail("abojeedwin@gmail.com");
        request.setPassword("SecurePassword123!");
        CreateAccountRes response = talentService.signup(request);
        Talent verifiedTalent = talentService.verifyEmail(response.getToken());
        assertEquals(TalentStatus.VERIFIED, verifiedTalent.getStatus());

        LoginTalentReq req = new LoginTalentReq();
        req.setEmail("abojeedwin@gmail.com");
        req.setPassword("SecurePassword123!");
        LoginTalentRes res = talentService.login(req);
        String token = res.getToken();
        assertNotNull(token);

        talentService.logout(token);

        assertThrows(TOKEN_INVALID.class, () -> talentService.logout(token));
    }

}