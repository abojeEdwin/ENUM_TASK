package com.EnumDayTask.data.repositories;


import com.EnumDayTask.data.model.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TalentRepo extends JpaRepository<Talent, Long> {
    Optional<Talent> findByEmail(String email);
    boolean existsByEmail(String email);

}
