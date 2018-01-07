package com.mwroblewski.repository;

import com.mwroblewski.model.ProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileDTORepository extends JpaRepository<ProfileDTO, Long> {
    ProfileDTO findById(Long id);
    ProfileDTO findByEmail(String email);
}
