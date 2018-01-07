package com.mwroblewski.validation;

import com.mwroblewski.bean.User;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.UserDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidationService {

    @Autowired
    UserDTORepository userDTORepository;

    Logger logger = LoggerFactory.getLogger(UserValidationService.class);

    private boolean areAllRequiredFields(User user){
        if(user.getUsername() == null){
            logger.error("Nazwa użytkownika jest wymagana.");
            throw new GeneralException("Nazwa użytkownika jest wymagana.");
        }
        if(user.getPassword() == null){
            logger.error("Hasło jest wymagane.");
            throw new GeneralException("Hasło jest wymagane.");
        }

        return true;
    }

    private boolean areUniqueAllRequiredFields(User user){
        UserDTO userDTO = userDTORepository.findByUsername(user.getUsername());
        if(userDTO != null){
            logger.info("Nazwa użytkownika: " + user.getUsername() + " jest już zajęta.");
            throw new InfoException("Nazwa użytkownika: " + user.getUsername() + " jest już zajęta.");
        }

        return true;
    }

    private boolean isSyntaxCorrect(User user){
        if(!Pattern.matches("[a-zA-Z0-9_]{8,50}", user.getUsername().trim())){
            logger.error("Niepoprawna nazwa użytkownika. [a-zA-Z0-9_\\.]{8,50}");
            throw new GeneralException("Niepoprawna nazwa użytkownika. [a-zA-Z0-9_]{8,50}");
        }
        if(!Pattern.matches("[a-zA-Z0-9]{8,50}", user.getPassword().trim())) {
            logger.error("Niepoprawne hasło. [a-zA-Z0-9]{8,50}");
            throw new GeneralException("Niepoprawne hasło. [a-zA-Z0-9]{8,50}");
        }

        return true;
    }

    public boolean checkUserValidation(User user){

        return (areAllRequiredFields(user) && areUniqueAllRequiredFields(user) && isSyntaxCorrect(user)) ? true : false;
    }
}
