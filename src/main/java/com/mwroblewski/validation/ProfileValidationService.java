package com.mwroblewski.validation;

import com.mwroblewski.bean.Profile;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.ProfileDTO;
import com.mwroblewski.repository.ProfileDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Service
public class ProfileValidationService {

    @Autowired
    ProfileDTORepository profileDTORepository;

    Logger logger = LoggerFactory.getLogger(ProfileValidationService.class);

    private boolean areAllRequiredFields(Profile profile){
        if(profile.getName() == null){
            logger.error("Imię jest wymagane.");
            throw new GeneralException("Imię jest wymagane.");
        }
        if(profile.getSurname() == null){
            logger.error("Nazwisko jest wymagane.");
            throw new GeneralException("Nazwisko jest wymagane.");
        }
        if(profile.getEmail() == null){
            logger.error("Email jest wymagany.");
            throw new GeneralException("Email jest wymagany.");
        }

        return true;
    }

    private boolean areUniqueAllRequiredFields(Profile profile){
        if(profile.getEmail() != null) {
            ProfileDTO profileDTO = profileDTORepository.findByEmail(profile.getEmail());
            if (profileDTO != null) {
                logger.info("Email: " + profileDTO.getEmail() + " jest już zajęty.");
                throw new InfoException("Email: " + profileDTO.getEmail() + " jest już zajęty.");
            }
        }

        return true;

    }

    private boolean isSyntaxCorrect(Profile profile){
        if(profile.getName() != null && !Pattern.matches("[a-zA-Z\\s]{1,50}", profile.getName().trim())){
            logger.error("Niepoprawne imię użytkownika. [a-zA-Z\\s]{1,50}");
            throw new GeneralException("Niepoprawne imię użytkownika. [a-zA-Z\\s]{1,50}");
        }
        if(profile.getSurname() != null && !Pattern.matches("[a-zA-Z\\s-]{1,50}", profile.getSurname().trim())){
            logger.error("Niepoprawne nazwisko użytkownika. [a-zA-Z\\s-]{1,50}");
            throw new GeneralException("Niepoprawne nazwisko użytkownika. [a-zA-Z\\s-]{1,50}");
        }
        if(profile.getEmail() != null && !Pattern.matches("[a-zA-Z0-9\\._-]{1,38}@[a-z]{2,6}.[a-z]{2,4}", profile.getEmail().trim())){
            logger.error("Niepoprawny email. [a-zA-Z0-9\\._-]{1,38}@[a-z]{2,6}.[a-z]{2,4}");
            throw new GeneralException("Niepoprawny email. [a-zA-Z0-9\\._-]{1,38}@[a-z]{2,6}.[a-z]{2,4}");
        }
        if(profile.getPhone() != null && !Pattern.matches("[0-9\\+\\s-]{1,20}", profile.getPhone().trim())){
            logger.error("Niepoprawny numeru telefonu. [0-9\\+\\s-]{1,20}");
            throw new GeneralException("Niepoprawny numeru telefonu. [0-9-\\+\\s]{1,20}");
        }
        if(profile.getAddress() != null && !Pattern.matches("[a-zA-Z0-9\\.\\s-]{1,50}", profile.getAddress().trim())){
            logger.error("Niepoprawny adres. [a-zA-Z0-9\\.\\s-]{1,50}");
            throw new GeneralException("Niepoprawny adres. [a-zA-Z0-9\\.\\s-]{1,50}");
        }

        // contract, born automatically check by JSON parser in syntax correction

        if(profile.getMaxSalary() != null){
            System.out.println(profile.getMaxSalary().compareTo(new BigDecimal("999999.99")));
            if(profile.getMaxSalary().compareTo(new BigDecimal("999999.99")) == 1 || profile.getMaxSalary().compareTo(new BigDecimal("0.00")) == -1){
                logger.error("Niepoprawna wartość wynagrodzenia maksymalnego.");
                throw new GeneralException("Niepoprawna wartość wynagrodzenia maksymalnego.");
            }
        }
        if(profile.getMinSalary() != null){
            if(profile.getMinSalary().compareTo(new BigDecimal("999999.99")) == 1 || profile.getMinSalary().compareTo(new BigDecimal("0.00")) == -1){
                logger.error("Niepoprawna wartość wynagrodzenia minimalnego.");
                throw new GeneralException("Niepoprawna wartość wynagrodzenia minimalnego.");
            }
        }

        if(profile.getExperiences() != null && !Pattern.matches("[a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}", profile.getExperiences().trim())){
            logger.error("Niepoprawna treść doświadczenia. [a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}");
            throw new GeneralException("Niepoprawna treść doświadczenia. [a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}");
        }
        if(profile.getAccomplishments() != null && !Pattern.matches("[a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}", profile.getAccomplishments().trim())){
            logger.error("Niepoprawna treść osiąfnięć. [a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}");
            throw new GeneralException("Niepoprawna treść osiągnieć. [a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}");
        }
        if(profile.getInterests() != null && !Pattern.matches("[a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}", profile.getInterests().trim())){
            logger.error("Niepoprawna treść zainteresowań. [a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}");
            throw new GeneralException("Niepoprawna treść zainteresowań. [a-zA-Z0-9\\.\\,\\?\\+-_\"]{1,2000}");
        }

        return true;
    }

    public boolean checkProfileAddValidation(Profile profile){

        return (areAllRequiredFields(profile) && areUniqueAllRequiredFields(profile) && isSyntaxCorrect(profile)) ? true : false;
    }

    public boolean checkProfileUpdateValidation(Profile profile){

        return (areUniqueAllRequiredFields(profile) && isSyntaxCorrect(profile)) ? true : false;
    }

}
