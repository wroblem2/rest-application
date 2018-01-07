package com.mwroblewski.validation;

import com.mwroblewski.bean.Entry;
import com.mwroblewski.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EntryValidationService {

    Logger logger = LoggerFactory.getLogger(MessageValidationService.class);

    private boolean isSyntaxCorrect(Entry entry){
        if(entry.getComment() != null && !Pattern.matches("[a-zA-Z0-9\\s\\.\\,\\?\\+-_\"]{1,3000}", entry.getComment())){
            logger.error("Niepoprawna treść komentarza. [a-zA-Z0-9\\s\\.\\,\\?\\+-_\"]{1,3000}");
            throw new GeneralException("Niepoprawna treść komentarza. [a-zA-Z0-9\\s\\.\\,\\?\\+-_\"]{1,3000}");
        }

        return true;
    }

    public boolean checkEntryValidation(Entry entry){

        return isSyntaxCorrect(entry) ? true : false;
    }

}
