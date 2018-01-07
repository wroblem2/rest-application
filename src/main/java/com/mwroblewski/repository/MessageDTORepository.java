package com.mwroblewski.repository;

import com.mwroblewski.model.MessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDTORepository extends JpaRepository<MessageDTO, Long> {
    MessageDTO findById(Long id);
}
