package com.mwroblewski.validation;

import com.mwroblewski.bean.Category;
import com.mwroblewski.bean.City;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.CategoryDTO;
import com.mwroblewski.model.CityDTO;
import com.mwroblewski.repository.CategoryDTORepository;
import com.mwroblewski.repository.CityDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryValidationService {

    @Autowired
    CategoryDTORepository categoryDTORepository;

    Logger logger = LoggerFactory.getLogger(CategoryValidationService.class);

    public boolean isExist(Category category){
          CategoryDTO categoryDTO = categoryDTORepository.findByName(category.getName().trim().toUpperCase());
          if(categoryDTO != null)
              return true;

        logger.info("Kategoria " + category.getName() + " nie jest dostępne.");
        throw new InfoException("Kategoria " + category.getName() + " nie jest dostępne.");
    }
}
