package com.mwroblewski.repository;

import com.mwroblewski.model.ActiveDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveDTORepository extends JpaRepository<ActiveDTO, Long> {
    ActiveDTO findById(Long id);
}
