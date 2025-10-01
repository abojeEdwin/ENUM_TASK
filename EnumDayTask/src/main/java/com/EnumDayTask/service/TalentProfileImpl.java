package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.EnumDayTask.data.model.TalentProfile;
import com.EnumDayTask.data.repositories.TalentProfileRepo;
import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.EnumDayTask.util.AppUtils.USER_NOT_FOUND;

@Service
public class TalentProfileImpl implements TalentProfileService {
    @Autowired
    TalentProfileRepo talentProfileRepo;

    @Override
    public TalentProfile updateProfile(UpdateProfileRequest request) {
        TalentProfile profile = talentProfileRepo.findById(request.getTalentId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        profile.setTranscript(request.getTranscript());
        profile.setStatementOfPurpose(request.getStatementOfPurpose());
        int completeness = 0;
        if (profile.getTranscript() != null && !profile.getTranscript().isEmpty()) {
            completeness += 50;}
        if (profile.getStatementOfPurpose() != null && !profile.getStatementOfPurpose().isEmpty()) {
            completeness += 50;}
        if (completeness == 100) {
            profile.setProfileCompleteness(ProfileCompleteness.HUNDRED);
        } else if (completeness == 50) {
            profile.setProfileCompleteness(ProfileCompleteness.FIFTY);
        } else {
            profile.setProfileCompleteness(ProfileCompleteness.ZERO);
        }
        return talentProfileRepo.save(profile);
    }

    @Override
    public void deleteAll() {
        talentProfileRepo.deleteAll();
    }
}
