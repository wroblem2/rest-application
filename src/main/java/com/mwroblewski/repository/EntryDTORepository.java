package com.mwroblewski.repository;

import com.mwroblewski.model.EntryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryDTORepository extends JpaRepository<EntryDTO, Long> {
    EntryDTO findById(Long id);
}
