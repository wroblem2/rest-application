package com.mwroblewski.validation;

import com.mwroblewski.bean.Offer;
import com.mwroblewski.bean.Technology;
import com.mwroblewski.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Service
public class OfferValidationService {

    @Autowired
    CityValidationService cityValidationService;
    @Autowired
    CategoryValidationService categoryValidationService;

    Logger logger = LoggerFactory.getLogger(MessageValidationService.class);

    private boolean allAllRequiredFields(Offer offer){
        if(offer.getName() == null){
            logger.error("Tytuł oferty jest wymagany.");
            throw new GeneralException("Tytuł oferty jest wymagany.");
        }
        if(offer.getDescription() == null){
            logger.error("Opis oferty jest wymagany.");
            throw new GeneralException("Opis oferty jest wymagany.");
        }

        return true;
    }

    private boolean isSyntaxCorrect(Offer offer){
        if(offer.getName() != null && !Pattern.matches("[a-zA-Z0-9\\.\\s]{1,500}", offer.getName().trim())){
            logger.error("Niepoprawny tytuł ofert. [a-zA-Z0-9\\s]{1,3000}");
            throw new GeneralException("Niepoprawny tytuł ofert. [a-zA-Z0-9\\s]{1,3000}");
        }
        if(offer.getDescription() != null && !Pattern.matches("[a-zA-Z0-9\\.\\s]{1,5000}", offer.getDescription().trim())){
            logger.error("Niepoprawny opis ofert. [a-zA-Z0-9\\s]{1,3000}");
            throw new GeneralException("Niepoprawny opis ofert. [a-zA-Z0-9\\s]{1,3000}");
        }

        // contract, created automatically check by JSON parser in syntax correction

        if(offer.getMaxSalary() != null){
            System.out.println(offer.getMaxSalary().compareTo(new BigDecimal("999999.99")));
            if(offer.getMaxSalary().compareTo(new BigDecimal("999999.99")) == 1 || offer.getMaxSalary().compareTo(new BigDecimal("0.00")) == -1){
                logger.error("Niepoprawna wartość wynagrodzenia maksymalnego.");
                throw new GeneralException("Niepoprawna wartość wynagrodzenia maksymalnego.");
            }
        }
        if(offer.getMinSalary() != null){
            if(offer.getMinSalary().compareTo(new BigDecimal("999999.99")) == 1 || offer.getMinSalary().compareTo(new BigDecimal("0.00")) == -1){
                logger.error("Niepoprawna wartość wynagrodzenia minimalnego.");
                throw new GeneralException("Niepoprawna wartość wynagrodzenia minimalnego.");
            }
        }
        if(offer.getCity() != null)
            cityValidationService.isExist(offer.getCity());

        if(offer.getTechnologies() != null){
            for(Technology t : offer.getTechnologies())
                categoryValidationService.isExist(t.getCategory());
        }

        return true;
    }

    public boolean checkOfferAddValidation(Offer offer){

        return (allAllRequiredFields(offer) && isSyntaxCorrect(offer)) ? true : false;
    }

    public boolean checkOfferUpdateValidation(Offer offer){
        return isSyntaxCorrect(offer) ? true : false;
    }

}
