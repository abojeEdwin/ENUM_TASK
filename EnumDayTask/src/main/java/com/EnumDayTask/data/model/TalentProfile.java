package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.ProfileCompleteness;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table(name = "talent_profile")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TalentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long talentId;
    private String transcript;
    private String statementOfPurpose;
    ProfileCompleteness profileCompleteness;

}
