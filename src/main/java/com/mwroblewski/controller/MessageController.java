package com.mwroblewski.controller;

import com.mwroblewski.bean.Message;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.service.LoggerDTOService;
import com.mwroblewski.service.MessageDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageDTOService messageDTOService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/getAll")
    public Set<Message> getUserAllMessages(@AuthenticationPrincipal Principal principal){
        return messageDTOService.getUserAllMessages(principal.getName());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/getByUserId")
    public Set<Message> getUserAllMessagesByUserId(@AuthenticationPrincipal Principal principal, @RequestParam(name = "id", required = true) Long id){
        return messageDTOService.getUserAllMessagesByUserId(principal.getName(), id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/send")
    public void sendMessageToUserById(@AuthenticationPrincipal Principal principal, @RequestParam(name = "id", required = true) Long id, @RequestBody Message message){
        messageDTOService.sendMessageToUserById(principal.getName(), id, message);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/deleteById")
    public void deleteUserMessageById(@AuthenticationPrincipal Principal principal, @RequestParam(name = "id", required = true) Long id){
        messageDTOService.deleteUserMessageById(principal.getName(), id);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping("/deleteAll")
    public void deleteUserAllMessage(@AuthenticationPrincipal Principal principal){
        messageDTOService.deleteUserAllMessages(principal.getName());
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
