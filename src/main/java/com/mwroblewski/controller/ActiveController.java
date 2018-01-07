package com.mwroblewski.controller;

import com.mwroblewski.bean.Active;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.service.ActiveDTOService;
import com.mwroblewski.service.LoggerDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/active")
public class ActiveController {

    @Autowired
    ActiveDTOService activeDTOService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping("getAll")
    public List<Active> getAll(){
        return activeDTOService.getAllActives();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("getById")
    public Active getByUserId(@RequestParam(name = "id", required = true) Long id){
        return activeDTOService.getUserActivesById(id);
    }

    @Autowired
    LoggerDTOService loggerDTOService;

    @ExceptionHandler(InfoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody Error infoException(InfoException e){
        loggerDTOService.persistLogger(e.getMessage(), e.getClass().toString());
        return new Error(e.getComment());
    }

    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Error generalException(GeneralException e){
        loggerDTOService.persistLogger(e.getComment(), e.getClass().toString());
        return new Error(e.getComment());
    }
}


