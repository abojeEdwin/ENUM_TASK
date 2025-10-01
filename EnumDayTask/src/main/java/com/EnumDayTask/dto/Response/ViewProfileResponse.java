package com.EnumDayTask.dto.Response;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewProfileResponse {
    private String email;
    private ProfileCompleteness completeness;
    private List<String> missingFields;
    private String transcript;
    private String statementOfPurpose;
}
