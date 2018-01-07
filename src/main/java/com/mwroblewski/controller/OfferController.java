package com.mwroblewski.controller;

import com.mwroblewski.bean.Offer;
import com.mwroblewski.exception.Error;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.service.LoggerDTOService;
import com.mwroblewski.service.OfferDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    OfferDTOService offerDTOService;

    @GetMapping()
    public List<Offer> getAllOffers(){
        return offerDTOService.getAllOffers();
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/get")
    public Set<Offer> getUserAllOffer(@AuthenticationPrincipal Principal principal){
        return offerDTOService.getUserAllOffers(principal.getName());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/add")
    public void addOffer(@AuthenticationPrincipal Principal principal, @RequestBody Offer offer){
        offerDTOService.addUserOffer(principal.getName(), offer);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/search")
    public List<Offer> addOffer(@RequestBody Offer offer){
        return offerDTOService.searchOffer(offer);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/deleteById")
    public void deleteOffer(@AuthenticationPrincipal Principal principal,@RequestParam(name = "id", required = true) Long id){
        offerDTOService.deleteUserOfferById(principal.getName(), id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/deleteById/admin")
    public void deleteOffer(@RequestParam(name = "id", required = true) Long id){
        offerDTOService.deleteOneById(id);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/deleteAll/admin")
    public void deleteAllOffer(@RequestParam(name = "ids", required = true) String ids){
        offerDTOService.deleteOffers(ids);
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
