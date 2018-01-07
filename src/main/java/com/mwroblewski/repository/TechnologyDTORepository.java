package com.mwroblewski.repository;

import com.mwroblewski.model.TechnologyDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyDTORepository extends JpaRepository<TechnologyDTO, Long> {
    TechnologyDTO findById(Long id);
}
