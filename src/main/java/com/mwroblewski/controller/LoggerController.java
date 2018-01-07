package com.mwroblewski.controller;

import com.mwroblewski.model.LoggerDTO;
import com.mwroblewski.service.LoggerDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logger")
public class LoggerController {

    @Autowired
    LoggerDTOService loggerDTOService;

    @GetMapping()
    public List<LoggerDTO> getAllLoggerr(){
        return loggerDTOService.getAllLoggers();
    }

    @DeleteMapping("/delete")
    public void deleteAllLoggers(){
        loggerDTOService.deleteAllLoggers();
    }

}
