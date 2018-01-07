package com.mwroblewski.service;

import com.mwroblewski.bean.Entry;
import com.mwroblewski.bean.Offer;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.EntryDTO;
import com.mwroblewski.model.OfferDTO;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.EntryDTORepository;
import com.mwroblewski.repository.OfferDTORepository;
import com.mwroblewski.repository.UserDTORepository;
import com.mwroblewski.validation.EntryValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class EntryDTOService {

    @Autowired
    EntryDTORepository entryDTORepository;
    @Autowired
    UserDTORepository userDTORepository;
    @Autowired
    OfferDTORepository offerDTORepository;
    @Autowired
    OfferDTOService offerDTOService;
    @Autowired
    EntryValidationService entryValidationService;
    @Autowired
    ActiveDTOService activeDTOService;

    private Logger logger = LoggerFactory.getLogger(EntryDTOService.class);

    private EntryDTO fromEntryToEntryDTO(Entry entry){
        EntryDTO entryDTO = new EntryDTO();

        entryDTO.setComment(entry.getComment());
        entryDTO.setApplied(new Date());

        return entryDTO;
    }

    public Entry fromEntryDTOToEntry(EntryDTO entryDTO){
        Entry entry = new Entry();

        entry.setId(entryDTO.getId());
        entry.setComment(entryDTO.getComment());
        entry.setApplied(entryDTO.getApplied());

        return entry;
    }

    @Transactional
    public Set<Entry> getAllUserEntry(String username){
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        Set<EntryDTO> entryDTOS = userDTO.getEntryDTOS();

        if(entryDTOS.size() > 0) {
            Set<Entry> entries = new LinkedHashSet<>();
            for (EntryDTO e : entryDTOS) {
                Entry entry = fromEntryDTOToEntry(e);
                Offer offer = offerDTOService.fromOfferDTOToOffer(e.getOfferDTO());
                offerDTOService.addInfoAboutUser(e.getOfferDTO(), offer);
                entry.setOffer(offer);

                entries.add(entry);
            }

            return entries;
        }

        logger.info("Użytkownik " + username + " nie posiada żadnego zgłoszenia.");
        throw new InfoException("Użytkownik " + username + " nie posiada żadnego zgłoszenia.");

    }

    @Transactional
    public void addUserEntry(String username, Long id, Entry entry){
        activeDTOService.updateActive(username);

        entryValidationService.checkEntryValidation(entry);
        UserDTO userDTO = userDTORepository.findByUsername(username);
        OfferDTO offerDTO = offerDTORepository.findById(id);

        if (offerDTO!= null){
            Set<EntryDTO> tmpEntryDTOS = offerDTO.getEntryDTOS();
            Optional<EntryDTO> optionalEntryDTO = tmpEntryDTOS
                                                .stream()
                                                .filter( e -> e.getUserDTO().getId() == userDTO.getId())
                                                .findFirst();

            if(optionalEntryDTO.isPresent()){
                logger.error("Użytkownik złożył już zgłoszenie do oferty zgodnej z id: " + id + ".");
                throw new InfoException("Użytkownik złożył już zgłoszenie do oferty zgodnej z id: " + id + ".");
            }

            EntryDTO entryDTO = fromEntryToEntryDTO(entry);
            entryDTO.setUserDTO(userDTO);
            entryDTO.setOfferDTO(offerDTO);
            offerDTO.getEntryDTOS().add(entryDTO);
            userDTO.getEntryDTOS().add(entryDTO);

            entryDTORepository.save(entryDTO);
        }
        else{
            logger.error("Oferta zgodna z id: " + id + " nie istanieje.");
            throw new GeneralException("Oferta zgodna z id: " + id + " nie istanieje.");
        }
    }

    @Transactional
    public void deleteUserEntryById(String username, Long id){
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        Set<EntryDTO> entryDTOS = userDTO.getEntryDTOS();
        Optional<EntryDTO> optionalEntryDTO = entryDTOS
                                                .stream()
                                                .filter( e -> e.getId().equals(id))
                                                .findFirst();

        if(optionalEntryDTO.isPresent()) {
            userDTO.getEntryDTOS().remove(optionalEntryDTO.get());
            entryDTORepository.delete(optionalEntryDTO.get());

        }
        else{
            logger.error("Użytkownik " + username + " nie posiada zgłoszenia zgodnego z id: " + id + ".");
            throw new GeneralException("Użytkownik " + username + " nie posiada zgłoszenia zgodnego z id: " + id + ".");
        }
    }

}
