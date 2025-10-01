package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.EnumDayTask.data.Enum.TalentStatus;
import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Request.LoginTalentReq;
import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.dto.Response.LoginTalentRes;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;
import com.EnumDayTask.dto.Response.ViewProfileResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TalentProfileImplTest {

    @Autowired
    private TalentProfileService talentProfileService;
    @Autowired
    private TalentAuthService talentService;

    @BeforeEach
    public void setUp() {
        talentProfileService.deleteAll();
        talentService.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        talentProfileService.deleteAll();
        talentService.deleteAll();
    }

    @Test
    public void testAuthenticatedUserProfileCreatedWith100PercentCompleteness() {
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

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setTalentId(verifiedTalent.getId());
        updateProfileRequest.setTranscript("Moving forward");
        updateProfileRequest.setStatementOfPurpose("Japa");
        UpdateProfileResponse updateProfileResponse = talentProfileService.updateProfile(updateProfileRequest);
        assertNotNull(updateProfileResponse);
        assertEquals(ProfileCompleteness.HUNDRED, updateProfileResponse.getProfileCompleteness());
        assertTrue(updateProfileResponse.getMissingFields().isEmpty());
    }

    @Test
    public void testAuthenticatedUserProfileCreatedWith50PercentCompleteness() {
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

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setTalentId(verifiedTalent.getId());
        updateProfileRequest.setTranscript("Moving forward");
        UpdateProfileResponse updateProfileResponse = talentProfileService.updateProfile(updateProfileRequest);
        assertNotNull(updateProfileResponse);
        assertEquals(ProfileCompleteness.FIFTY, updateProfileResponse.getProfileCompleteness());
        assertTrue(updateProfileResponse.getMissingFields().contains("statementOfPurpose"));
        assertEquals(1, updateProfileResponse.getMissingFields().size());
    }

    @Test
    public void testAuthenticatedUserProfileCreatedWith0PercentCompleteness() {
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

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setTalentId(verifiedTalent.getId());
        UpdateProfileResponse updateProfileResponse = talentProfileService.updateProfile(updateProfileRequest);
        assertNotNull(updateProfileResponse);
        assertEquals(ProfileCompleteness.ZERO, updateProfileResponse.getProfileCompleteness());
        assertTrue(updateProfileResponse.getMissingFields().contains("transcript"));
        assertTrue(updateProfileResponse.getMissingFields().contains("statementOfPurpose"));
        assertEquals(2, updateProfileResponse.getMissingFields().size());
    }

    @Test
    public void testAuthenticatedUserViewProfile(){
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

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setTalentId(verifiedTalent.getId());
        updateProfileRequest.setTranscript("Moving forward");
        updateProfileRequest.setStatementOfPurpose("Japa");
        UpdateProfileResponse updateProfileResponse = talentProfileService.updateProfile(updateProfileRequest);
        assertNotNull(updateProfileResponse);
        assertEquals(ProfileCompleteness.HUNDRED, updateProfileResponse.getProfileCompleteness());
        assertTrue(updateProfileResponse.getMissingFields().isEmpty());

        ViewProfileResponse viewProfileResponse = talentProfileService.viewProfile(verifiedTalent.getId());
        assertNotNull(viewProfileResponse);
        assertEquals(verifiedTalent.getEmail(), viewProfileResponse.getEmail());
        assertEquals(ProfileCompleteness.HUNDRED, viewProfileResponse.getCompleteness());
        assertTrue(viewProfileResponse.getMissingFields().isEmpty());
        assertEquals("Moving forward", viewProfileResponse.getTranscript());
        assertEquals("Japa", viewProfileResponse.getStatementOfPurpose());

    }

    @Test
    public void testAuthenticatedUserReturnsMissingFieldsForIncompleteProfile(){
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


        ViewProfileResponse viewProfileResponse = talentProfileService.viewProfile(verifiedTalent.getId());
        assertNotNull(viewProfileResponse);
        assertEquals(verifiedTalent.getEmail(), viewProfileResponse.getEmail());
        assertEquals(ProfileCompleteness.ZERO, viewProfileResponse.getCompleteness());
        assertTrue(viewProfileResponse.getMissingFields().contains("transcript"));
        assertTrue(viewProfileResponse.getMissingFields().contains("statementOfPurpose"));
        assertEquals(2, viewProfileResponse.getMissingFields().size());

    }
}
