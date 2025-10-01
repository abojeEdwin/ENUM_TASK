package com.EnumDayTask.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotNull(message = "This field is required")
    private Long talentId;
    @NotNull(message = "This field is required")
    private String transcript;
    @NotNull(message = "This field is required")
    private String statementOfPurpose;

}
