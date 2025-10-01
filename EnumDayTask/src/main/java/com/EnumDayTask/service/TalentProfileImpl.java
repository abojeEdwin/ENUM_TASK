package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.EnumDayTask.data.model.TalentProfile;
import com.EnumDayTask.data.repositories.TalentProfileRepo;
import com.EnumDayTask.dto.Request.UpdateProfileRequest;
import com.EnumDayTask.dto.Response.UpdateProfileResponse;
import com.EnumDayTask.dto.Response.ViewProfileResponse;
import com.EnumDayTask.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.EnumDayTask.util.AppUtils.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class TalentProfileImpl implements TalentProfileService {

    private final TalentProfileRepo talentProfileRepo;

    @Override
    @Transactional
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
        talentProfileRepo.saveAndFlush(profile);

        return new UpdateProfileResponse("Profile updated successfully", completenessEnum, missingFields);
    }

    @Override
    public void deleteAll() {
        talentProfileRepo.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ViewProfileResponse viewProfile(long talentId) {
        TalentProfile profile = talentProfileRepo.findById(talentId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        List<String> missingFields = new ArrayList<>();
        if (profile.getTranscript() == null || profile.getTranscript().isEmpty()) {
            missingFields.add("transcript");
        }
        if (profile.getStatementOfPurpose() == null || profile.getStatementOfPurpose().isEmpty()) {
            missingFields.add("statementOfPurpose");
        }

        return new ViewProfileResponse(
                profile.getTalent().getEmail(),
                profile.getProfileCompleteness(),
                missingFields,
                profile.getTranscript(),
                profile.getStatementOfPurpose()
        );
    }
}
