package com.mwroblewski.repository;

import com.mwroblewski.model.OfferDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferDTORepository extends JpaRepository<OfferDTO, Long> {
    OfferDTO findById(Long id);
}
