package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.EnumDayTask.data.Enum.TalentStatus;
import com.EnumDayTask.data.model.Talent;
import com.EnumDayTask.data.model.TalentProfile;
import com.EnumDayTask.dto.Request.CreateAccountReq;
import com.EnumDayTask.dto.Request.LoginTalentReq;
import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.CreateAccountRes;
import com.EnumDayTask.dto.Response.LoginTalentRes;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TalentProfileImplTest {

    @Autowired
    private TalentProfileImpl talentProfileService;
    @Autowired
    private TalentAuthImpl talentService;

    @BeforeEach
    public void setUp(){
        talentProfileService.deleteAll();
        talentService.deleteAll();
    }

    @AfterEach
    public void tearDown(){
        talentProfileService.deleteAll();
        talentService.deleteAll();
    }

    @Test
    public void testAuthenticatedUserProfileCreatedWith100PercentCompleteness(){
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
        TalentProfile updateProfileResponse = talentProfileService.updateProfile(updateProfileRequest);
        assertNotNull(updateProfileResponse);
        assertEquals(updateProfileResponse.getProfileCompleteness(), ProfileCompleteness.HUNDRED);

    }

    @Test
    public void testAuthenticatedUserProfileCreatedWith50PercentCompleteness(){
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
        TalentProfile updateProfileResponse = talentProfileService.updateProfile(updateProfileRequest);
        assertNotNull(updateProfileResponse);
        assertEquals(updateProfileResponse.getProfileCompleteness(), ProfileCompleteness.FIFTY);

    }

}