package com.EnumDayTask.service;

import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;

public interface TalentProfileService {

    UpdateProfileResponse updateProfile(UpdateProfileRequest request);
    void deleteAll();
}
