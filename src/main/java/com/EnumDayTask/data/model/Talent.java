package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.TalentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "talent")
@Getter
@Setter
public class Talent implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private TalentStatus status;

    private int failedLoginAttempts;
    private LocalDateTime lockoutTime;

    @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TalentProfile talentProfile;

    public void setTalentProfile(TalentProfile talentProfile) {
        if (talentProfile == null) {
            if (this.talentProfile != null) {
                this.talentProfile.setTalent(null);
            }
        } else {
            talentProfile.setTalent(this);
        }
        this.talentProfile = talentProfile;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
