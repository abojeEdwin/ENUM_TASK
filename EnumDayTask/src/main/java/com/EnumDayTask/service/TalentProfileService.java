package com.EnumDayTask.service;

import com.EnumDayTask.data.model.TalentProfile;
import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;

public interface TalentProfileService {

    TalentProfile updateProfile(UpdateProfileRequest request);
    void deleteAll();
}
