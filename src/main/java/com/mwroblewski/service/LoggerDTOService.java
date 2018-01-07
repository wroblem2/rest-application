package com.mwroblewski.service;

import com.mwroblewski.model.LoggerDTO;
import com.mwroblewski.repository.LoggerDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoggerDTOService {

    @Autowired
    LoggerDTORepository loggerDTORepository;

    Logger logger = LoggerFactory.getLogger(LoggerDTOService.class);

    @Transactional
    public void persistLogger(String message, String type){
        LoggerDTO loggerDTO = new LoggerDTO();

        loggerDTO.setMessage(message);
        loggerDTO.setType(type);

        loggerDTORepository.save(loggerDTO);
    }

    @Transactional
    public List<LoggerDTO> getAllLoggers(){
        List<LoggerDTO> loggerDTOS = loggerDTORepository.findAll();

        if(loggerDTOS.size() == 0)
            logger.info("Aktualnie brak logów.");

            return loggerDTOS;
    }

    @Transactional
    public void deleteAllLoggers(){
        loggerDTORepository.deleteAll();
    }
}
