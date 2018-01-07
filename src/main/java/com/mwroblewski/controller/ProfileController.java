package com.mwroblewski.controller;

import com.mwroblewski.bean.Profile;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.ProfileDTO;
import com.mwroblewski.service.LoggerDTOService;
import com.mwroblewski.service.ProfileDTOService;
import com.mwroblewski.validation.ProfileValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileDTOService profileDTOService;
    @Autowired
    ProfileValidationService profileValidationService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping()
    public List<Profile> getAllProfiles() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return profileDTOService.getAllProfiles();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/get")
    public Profile getUserProfile(@AuthenticationPrincipal Principal principal) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return profileDTOService.getUserProfile(principal.getName());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/getById")
    public Profile getUserProfileById(@RequestParam(name = "id", required = true) Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return profileDTOService.getUserProfileById(id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/update")
    public void updateUserProfile(@AuthenticationPrincipal Principal principal, @RequestBody Profile profile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        profileDTOService.updateUserProfile(principal.getName(), profile);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/add")
    public void addProfile(@AuthenticationPrincipal Principal principal, @RequestBody Profile profile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        profileDTOService.addProfile(principal.getName(), profile);
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
