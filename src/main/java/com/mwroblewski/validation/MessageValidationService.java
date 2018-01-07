package com.mwroblewski.validation;

import com.mwroblewski.bean.Message;
import com.mwroblewski.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class MessageValidationService{

    Logger logger = LoggerFactory.getLogger(MessageValidationService.class);

    private boolean allAllRequiredFields(Message message){
        if(message.getText() == null){
            logger.error("Treść wiadomości jest wymagana.");
            throw new GeneralException("Treść wiadomości jest wymagana.");
        }

        return true;
    }

    private boolean isSyntaxCorrect(Message message){
        if(!Pattern.matches("[a-zA-Z0-9\\s\\.\\,\\?\\+-_\"]{1,3000}", message.getText())){
            logger.error("Niepoprawna treść wiadomośći. [a-zA-Z0-9\\s\\.\\,\\?\\+-_\"]{1,3000}");
            throw new GeneralException("Niepoprawna treść wiadomości. [a-zA-Z0-9\\s\\.\\,\\?\\+-_\"]{1,3000}");
        }

        return true;
    }

    public boolean checkMessageValidation(Message message){

        return (allAllRequiredFields(message) && isSyntaxCorrect(message)) ? true : false;
    }
}
