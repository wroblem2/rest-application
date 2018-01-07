package com.mwroblewski.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwroblewski.bean.Profile;
import com.mwroblewski.bean.User;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.service.ActiveDTOService;
import com.mwroblewski.service.LoggerDTOService;
import com.mwroblewski.service.ProfileDTOService;
import com.mwroblewski.service.UserDTOService;
import com.mwroblewski.validation.ProfileValidationService;
import com.mwroblewski.validation.UserValidationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDTOService userDTOService;
    @Autowired
    ProfileDTOService profileDTOService;
    @Autowired
    UserValidationService userValidationService;
    @Autowired
    ActiveDTOService activeDTOService;
    @Autowired
    ProfileValidationService profileValidationService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping("/log")
    public void login(@AuthenticationPrincipal Principal principal){
        activeDTOService.updateSignIn(principal.getName());
    }
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping("/logout")
    public void loginout(@AuthenticationPrincipal Principal principal){
        activeDTOService.updateSignOut(principal.getName());
    }

    @PostMapping("/reg")
    public void register(@RequestBody User user){
        userDTOService.regUser(user);
    }

    @PostMapping("/regWithProf")
    public void register(@RequestBody List<HashMap<String, Object>> detail) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject userObject = new JSONObject(detail.get(0));
        JSONObject profileObject = new JSONObject(detail.get(1));

        User user = objectMapper.readValue(userObject.toString(), User.class);
        Profile profile = objectMapper.readValue(profileObject.toString(), Profile.class);

        userDTOService.regUserWithProfile(user, profile);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/user/add")
    public void addUser(@AuthenticationPrincipal Principal principal, @RequestBody User user){
        userDTOService.addUser(principal.getName(), user);
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/user/update")
    public void updateUser(@AuthenticationPrincipal Principal principal,@RequestParam(name = "id", required = true) Long id, @RequestBody User user){
        userDTOService.updateUser(principal.getName(), id, user);
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
