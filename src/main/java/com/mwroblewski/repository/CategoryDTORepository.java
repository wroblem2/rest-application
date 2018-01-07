package com.mwroblewski.repository;

import com.mwroblewski.model.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDTORepository extends JpaRepository<CategoryDTO, Long> {
    CategoryDTO findById(Long id);
    CategoryDTO findByName(String name);
}
