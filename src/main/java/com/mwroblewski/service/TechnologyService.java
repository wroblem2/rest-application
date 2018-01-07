package com.mwroblewski.service;

import com.mwroblewski.bean.Category;
import com.mwroblewski.bean.Technology;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.model.CategoryDTO;
import com.mwroblewski.model.TechnologyDTO;
import com.mwroblewski.repository.TechnologyDTORepository;
import com.mwroblewski.validation.CategoryValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechnologyService {

    @Autowired
    TechnologyDTORepository technologyDTORepository;
    @Autowired
    CategoryDTOService categoryDTOService;
    @Autowired
    CategoryValidationService categoryValidationService;

    private Logger logger = LoggerFactory.getLogger(TechnologyService.class);

    public TechnologyDTO fromTechnologyToTechnologyDTO(Technology technology){
        TechnologyDTO technologyDTO = new TechnologyDTO();

        technologyDTO.setLevel(technology.getLevel());
        Category category = technology.getCategory();
        categoryValidationService.isExist(category);
        technologyDTO.setCategoryDTO(categoryDTOService.fromCategoryToCategoryDTO(category));

        return technologyDTO;
    }

    public Technology fromTechnologyDTOToTechnology(TechnologyDTO technologyDTO){
        Technology technology = new Technology();

        technology.setId(technologyDTO.getId());
        technology.setLevel(technologyDTO.getLevel());
        Category category = categoryDTOService.fromCategoryDTOToCategory(technologyDTO.getCategoryDTO());
        technology.setCategory(category);

        return technology;
    }

    public TechnologyDTO updateTechnology(Technology technology){
        if(technology.getId() == null){
            TechnologyDTO technologyDTO = fromTechnologyToTechnologyDTO(technology);
            return technologyDTO;
        }

        TechnologyDTO technologyDTO = technologyDTORepository.findById(technology.getId());

        if(technology.getLevel() != null)
            technologyDTO.setLevel(technology.getLevel());
        if(technology.getClass() != null) {
            CategoryDTO categoryDTO = categoryDTOService.fromCategoryToCategoryDTO(technology.getCategory());
            technologyDTO.setCategoryDTO(categoryDTO);

            technologyDTORepository.save(technologyDTO);
        }

        return null;
    }
}
