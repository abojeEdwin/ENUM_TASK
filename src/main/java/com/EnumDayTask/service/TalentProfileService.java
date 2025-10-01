package com.EnumDayTask.service;

import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;
import com.EnumDayTask.dto.Response.ViewProfileResponse;

public interface TalentProfileService {

    UpdateProfileResponse updateProfile(UpdateProfileRequest request);
    void deleteAll();
    ViewProfileResponse viewProfile(long talentId);
}
