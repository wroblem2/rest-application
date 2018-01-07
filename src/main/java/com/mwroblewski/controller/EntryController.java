package com.mwroblewski.controller;

import com.mwroblewski.bean.Entry;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.service.ActiveDTOService;
import com.mwroblewski.service.EntryDTOService;
import com.mwroblewski.service.LoggerDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/entry")
public class EntryController {

    @Autowired
    EntryDTOService entryDTOService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/get")
    public Set<Entry> getAllUserEntry(@AuthenticationPrincipal Principal principal){
        return entryDTOService.getAllUserEntry(principal.getName());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/add")
    public void addUserEntry(@AuthenticationPrincipal Principal principal, @RequestParam(name = "id", required = true) Long id, @RequestBody Entry entry){
        entryDTOService.addUserEntry(principal.getName(), id, entry);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/deleteById")
    public void deleteUserEntryById(@AuthenticationPrincipal Principal principal, @RequestParam(name = "id", required = true) Long id){
        entryDTOService.deleteUserEntryById(principal.getName(), id);
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
