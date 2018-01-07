package com.mwroblewski.validation;

import com.mwroblewski.bean.City;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.CityDTO;
import com.mwroblewski.repository.CityDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityValidationService {

    @Autowired
    CityDTORepository cityDTORepository;

    Logger logger = LoggerFactory.getLogger(CityValidationService.class);

    public boolean isExist(City city){
          CityDTO cityDTO = cityDTORepository.findByName(city.getName().trim().toUpperCase());
          if(cityDTO != null)
              return true;

        logger.info("Miasto " + city.getName() + " nie jest dostępne.");
        throw new InfoException("Miasto " + city.getName() + " nie jest dostępne.");
    }
}
