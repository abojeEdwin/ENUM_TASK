package com.EnumDayTask.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileResponse {

    private Long talentId;
    private Long profileId;
    private String transcript;
    private String statementOfPurpose;

}
