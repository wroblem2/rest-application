package com.mwroblewski.repository;

import com.mwroblewski.model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDTORepository extends JpaRepository<UserDTO, Long> {
    UserDTO findById(Long id);
    UserDTO findByUsername(String username);
}
