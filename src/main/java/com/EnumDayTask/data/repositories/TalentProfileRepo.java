package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.TalentProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalentProfileRepo extends JpaRepository<TalentProfile, Long> {
    Optional<TalentProfile> findById(Long talentId);
}
