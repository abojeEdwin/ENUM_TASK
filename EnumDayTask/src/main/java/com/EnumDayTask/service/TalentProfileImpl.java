package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.EnumDayTask.data.model.TalentProfile;
import com.EnumDayTask.data.repositories.TalentProfileRepo;
import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;
import com.EnumDayTask.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.EnumDayTask.util.AppUtils.PROFILE_UPDATED_SUCCESSFULLY;
import static com.EnumDayTask.util.AppUtils.USER_NOT_FOUND;

@Service
public class TalentProfileImpl implements TalentProfileService {
    @Autowired
    TalentProfileRepo talentProfileRepo;

    @Override
    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {
        TalentProfile profile = talentProfileRepo.findById(request.getTalentId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        profile.setTranscript(request.getTranscript());
        profile.setStatementOfPurpose(request.getStatementOfPurpose());

        List<String> missingFields = new ArrayList<>();
        int completeness = 0;
        if (profile.getTranscript() != null && !profile.getTranscript().isEmpty()) {
            completeness += 50;
        } else {
            missingFields.add("transcript");}
        if (profile.getStatementOfPurpose() != null && !profile.getStatementOfPurpose().isEmpty()) {
            completeness += 50;
        } else {
            missingFields.add("statementOfPurpose");}
        ProfileCompleteness completenessEnum;
        if (completeness == 100) {
            completenessEnum = ProfileCompleteness.HUNDRED;
        } else if (completeness == 50) {
            completenessEnum = ProfileCompleteness.FIFTY;
        } else {
            completenessEnum = ProfileCompleteness.ZERO;
        }
        profile.setProfileCompleteness(completenessEnum);
        talentProfileRepo.save(profile);
        return new UpdateProfileResponse(PROFILE_UPDATED_SUCCESSFULLY, completenessEnum, missingFields);
    }

    @Override
    public void deleteAll() {
        talentProfileRepo.deleteAll();
    }
}
