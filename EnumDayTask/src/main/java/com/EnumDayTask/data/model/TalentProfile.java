package com.EnumDayTask.data.model;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "talent_profile")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TalentProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Talent talent;

    private String transcript;
    private String statementOfPurpose;
    private ProfileCompleteness profileCompleteness;
    private List<String> missingFields;

    public TalentProfile(Talent talent) {
        this.talent = talent;
    }
}
