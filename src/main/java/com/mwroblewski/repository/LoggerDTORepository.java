package com.mwroblewski.repository;

import com.mwroblewski.model.LoggerDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerDTORepository extends JpaRepository<LoggerDTO, Long> {
}
