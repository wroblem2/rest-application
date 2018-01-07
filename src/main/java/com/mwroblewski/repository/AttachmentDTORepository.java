package com.mwroblewski.repository;

import com.mwroblewski.model.AttachmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentDTORepository extends JpaRepository<AttachmentDTO, Long> {
    AttachmentDTO findById(Long id);
}
