package com.mwroblewski.repository;

import com.mwroblewski.model.CityDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityDTORepository extends JpaRepository<CityDTO, Long> {
    CityDTO findById(Long id);
    CityDTO findByName(String name);
}
