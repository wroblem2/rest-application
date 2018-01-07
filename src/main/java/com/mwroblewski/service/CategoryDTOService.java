package com.mwroblewski.service;

import com.mwroblewski.bean.Category;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.CategoryDTO;
import com.mwroblewski.repository.CategoryDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryDTOService {

    @Autowired
    CategoryDTORepository categoryDTORepository;

    Logger logger = LoggerFactory.getLogger(CategoryDTOService.class);

    public CategoryDTO fromCategoryToCategoryDTO(Category category){
        CategoryDTO categoryDTO = categoryDTORepository.findByName(category.getName().toUpperCase());

        return categoryDTO;
    }

    public Category fromCategoryDTOToCategory(CategoryDTO categoryDTO){
        Category category = new Category();

        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        return category;
    }

    @Transactional
    public List<Category> getAllCategories(){
        List<CategoryDTO> categoryDTOS = categoryDTORepository.findAll();

        List<Category> categories = new ArrayList<>();
        for (CategoryDTO c : categoryDTOS) {
            categories.add(fromCategoryDTOToCategory(c));
        }

        if(categories.size() > 0)
            return categories;

        logger.info("Brak kategorii do wyświtlenia.");
        throw new GeneralException("Brak kategorii do wyświetlenia.");
    }

    @Transactional
    public Category getCategoryById(Long id){
        CategoryDTO categoryDTO = categoryDTORepository.findById(id);

        if(categoryDTO != null ){
            Category category = fromCategoryDTOToCategory(categoryDTO);
            return category;
        }

        logger.info("Kategoria zgodna z id: " + id + " nie istnieje.");
        throw new InfoException("Kategoria zgodna z id: " + id + " nie istnieje.");

    }

    @Transactional
    public void addCategory(Category category){
        CategoryDTO categoryDTO = categoryDTORepository.findByName(category.getName().trim().toUpperCase());
        if(categoryDTO == null){
            categoryDTO = fromCategoryToCategoryDTO(category);
            categoryDTORepository.save(categoryDTO);
        }
        else{
            logger.info("Kategoria " + category.getName() + " już istnieje.");
            throw new InfoException("Kategoria " + category.getName() + " już istnieje.");
        }
    }
}
