package com.mwroblewski.service;

import com.mwroblewski.bean.*;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.*;
import com.mwroblewski.repository.EntryDTORepository;
import com.mwroblewski.repository.OfferDTORepository;
import com.mwroblewski.repository.TechnologyDTORepository;
import com.mwroblewski.repository.UserDTORepository;
import com.mwroblewski.validation.OfferValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfferDTOService {

    @Autowired
    OfferDTORepository offerDTORepository;
    @Autowired
    UserDTORepository userDTORepository;
    @Autowired
    TechnologyDTORepository technologyDTORepository;
    @Autowired
    EntryDTORepository entryDTORepository;
    @Autowired
    TechnologyService technologyService;
    @Autowired
    EntryDTOService entryDTOService;
    @Autowired
    CityDTOService cityDTOService;
    @Autowired
    ActiveDTOService activeDTOService;
    @Autowired
    OfferValidationService offerValidationService;

    Logger logger = LoggerFactory.getLogger(OfferDTOService.class);

    private OfferDTO fromOfferToOfferDTO(Offer offer){
        OfferDTO offerDTO = new OfferDTO();

        offerDTO.setName(offer.getName().trim());
        offerDTO.setDescription(offer.getDescription().trim());
        offerDTO.setPublished(new Date());
        offerDTO.setContract(offer.getContract());
        offerDTO.setMaxSalary(offer.getMaxSalary());
        offerDTO.setMinSalary(offer.getMinSalary());
        Set<Technology> technologies = offer.getTechnologies();
        for (Technology t : technologies) {
            TechnologyDTO technologyDTO = technologyService.fromTechnologyToTechnologyDTO(t);
            technologyDTO.setOfferDTO(offerDTO);
            offerDTO.getTechnologies().add(technologyDTO);

        }
        if(offer.getCity() != null) {
            CityDTO cityDTO = cityDTOService.fromCityToCityDTO(offer.getCity());
            offerDTO.setCityDTO(cityDTO);
        }
        return offerDTO;
    }

    public Offer fromOfferDTOToOffer(OfferDTO offerDTO){
        Offer offer = new Offer();

        offer.setId(offerDTO.getId());
        offer.setName(offerDTO.getName());
        offer.setDescription(offerDTO.getDescription());
        offer.setPublished(offerDTO.getPublished());
        offer.setContract(offerDTO.getContract());
        offer.setMaxSalary(offerDTO.getMaxSalary());
        offer.setMinSalary(offerDTO.getMinSalary());
        Set<TechnologyDTO> technologyDTOS = offerDTO.getTechnologies();
        for (TechnologyDTO t : technologyDTOS) {
            Technology tmp = technologyService.fromTechnologyDTOToTechnology(t);
            offer.getTechnologies().add(tmp);
        }
        if(offerDTO.getCityDTO() != null) {
            City city = cityDTOService.fromCityDTOToCity(offerDTO.getCityDTO());
            offer.setCity(city);
        }

        return offer;
    }

    private void addInfoAboutEntries(OfferDTO offerDTO, Offer offer){
        Set<EntryDTO> entryDTOS = offerDTO.getEntryDTOS();
        for(EntryDTO e : entryDTOS){
            User user = new User(e.getUserDTO().getId(), e.getUserDTO().getUsername());
            Entry entry = entryDTOService.fromEntryDTOToEntry(e);
            entry.setUser(user);

            offer.getEntries().add(entry);
        }
    }

    public void addInfoAboutUser(OfferDTO offerDTO, Offer offer){
        UserDTO userDTO = offerDTO.getUserDTO();
        User user = new User(userDTO.getId(), userDTO.getUsername());

        offer.setUser(user);
    }

    private OfferDTO findOfferDTOById(Set<OfferDTO> offerDTOS, Long id) {
        Optional<OfferDTO> optionalOfferDTO = offerDTOS
                .stream()
                .filter(o -> o.getId() == id)
                .findFirst();

        if(optionalOfferDTO.isPresent())
            return optionalOfferDTO.get();
        else
            return null;
    }

    @Transactional
    public List<Offer> getAllOffers(){
        List<OfferDTO> offerDTOS = offerDTORepository.findAll();

        if(offerDTOS.size() > 0){
            List<Offer> offers = new ArrayList<>();
            for (OfferDTO o : offerDTOS) {
                Offer offer = fromOfferDTOToOffer(o);
                addInfoAboutUser(o, offer);

                offers.add(offer);
            }
            return offers;
        }

        logger.info("Brak ofert do wyświetlenia.");
        throw new InfoException("Brak ofert do wyświetlenia.");
    }

    @Transactional
    public Offer getUserOfferById(String username, Long id){
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        Set<OfferDTO> offerDTOS = userDTO.getOfferDTOS();

        OfferDTO offerDTO = findOfferDTOById(offerDTOS, id);
        if(offerDTO != null){
            Offer offer = fromOfferDTOToOffer(offerDTO);
            addInfoAboutEntries(offerDTO, offer);

            return offer;
        }

        logger.info("Użytkownik " + username + " nie posiada oferty zgodniej z id: " + id + ".");
        throw new InfoException("Użytkownik " + username + " nie posiada oferty zgodniej z id: " + id + ".");
    }

    @Transactional
    public Set<Offer> getUserAllOffers(String username){
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        Set<OfferDTO> offerDTOS = userDTO.getOfferDTOS();

        Set<Offer> offers = new LinkedHashSet<>();
        for (OfferDTO o : offerDTOS) {
            Offer offer = fromOfferDTOToOffer(o);
            addInfoAboutEntries(o, offer);

            offers.add(offer);
        }

        if(offers.size() > 0)
            return offers;

        logger.info("Użytkownik " + username + " nie posiada żadnej oferty.");
        throw new InfoException("Użytkownik " + username + " nie posiada żadnej oferty.");
    }

    @Transactional
    public void addUserOffer(String username, Offer offer){
        activeDTOService.updateActive(username);

        offerValidationService.checkOfferAddValidation(offer);
        UserDTO userDTO = userDTORepository.findByUsername(username);

        OfferDTO offerDTO = fromOfferToOfferDTO(offer);

        offerDTO.setUserDTO(userDTO);
        userDTO.getOfferDTOS().add(offerDTO);

        userDTORepository.save(userDTO);
    }

    @Transactional
    public List<Offer> searchOffer(Offer offer){
        List<Offer> tmpOffer = getAllOffers();
        if(tmpOffer.size() == 0){
            logger.info("Brak ofert.");
            throw new InfoException("Brak ofert.");
        }

        if(offer.getId() != null)
            tmpOffer = tmpOffer.stream()
                    .filter(o -> o.getId() == offer.getId())
                    .collect(Collectors.toList());


        if(offer.getName() != null)
            tmpOffer = tmpOffer.stream()
                    .filter( o -> o.getName().toUpperCase().contains(offer.getName().trim().toUpperCase()))
                    .collect(Collectors.toList());

        if(offer.getUser() != null)
            tmpOffer = tmpOffer.stream()
                    .filter(o -> o.getUser().getId() == offer.getUser().getId())
                    .collect(Collectors.toList());


        if(offer.getPublished() != null)
            tmpOffer = tmpOffer.stream()
                    .filter(o -> o.getPublished().compareTo(offer.getPublished()) != -1)
                    .collect(Collectors.toList());

        if(offer.getMaxSalary() != null)
            tmpOffer = tmpOffer.stream()
                    .filter( o -> o.getMaxSalary().compareTo(offer.getMaxSalary()) == 1)
                    .collect(Collectors.toList());

        if(offer.getMinSalary() != null)
            tmpOffer = tmpOffer.stream()
                    .filter( o -> o.getMinSalary().compareTo(offer.getMinSalary()) == -1)
                    .collect(Collectors.toList());

        if(offer.getContract() != null)
            tmpOffer = tmpOffer.stream()
                    .filter( o -> o.getContract().equals(offer.getContract()))
                    .collect(Collectors.toList());


        if(offer.getCity() != null)
            tmpOffer = tmpOffer.stream()
                    .filter( o -> (o.getCity().getName().toUpperCase().contains(offer.getCity().getName().trim().toUpperCase())))
                    .collect(Collectors.toList());

        if(tmpOffer.size() == 0){
            logger.info("Brak ofert.");
            throw new InfoException("Brak ofert.");
        }

        return tmpOffer;
    }

    @Transactional
    public void deleteUserOfferById(String username, Long id){
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);

        OfferDTO offerDTO = findOfferDTOById(userDTO.getOfferDTOS(), id);
        if(offerDTO != null){
            Set<EntryDTO> entryDTOS = offerDTO.getEntryDTOS();
            entryDTORepository.delete(entryDTOS);
            Set<TechnologyDTO> technologyDTOS = offerDTO.getTechnologies();
            technologyDTORepository.delete(technologyDTOS);
            userDTO.getOfferDTOS().remove(offerDTO);
            offerDTORepository.delete(offerDTO);

            userDTORepository.save(userDTO);
        }
        else {
            logger.info("Użytkownik " + username + " nie posiada oferty zgodniej z id: " + id + ".");
            throw new InfoException("Użytkownik " + username + " nie posiada oferty zgodniej z id: " + id + ".");
        }
    }

    @Transactional
    public void deleteOneById(Long id){
        OfferDTO offerDTO = offerDTORepository.findById(id);
        if(offerDTO == null){
            logger.info("Brak wiadomości zgodnej z id: " + id + ".");
            throw new InfoException("Brak wiadomości zgodnej z id: " + id + ".");
        }

        UserDTO userDTO = offerDTO.getUserDTO();
        userDTO.getOfferDTOS().remove(offerDTO);

        offerDTORepository.delete(offerDTO);
        userDTORepository.save(userDTO);
    }

    @Transactional
    public void deleteOffers(String ids){
       String[] id = ids.split(",");
       Set<String> incorrectId = new LinkedHashSet<>();
       for(String i : id){
           OfferDTO offerDTO = offerDTORepository.findById(Long.valueOf(i));
           if(offerDTO == null){
               incorrectId.add(i);
               continue;
           }

           UserDTO userDTO = offerDTO.getUserDTO();
           userDTO.getOfferDTOS().remove(offerDTO);

           offerDTORepository.delete(offerDTO);
           userDTORepository.save(userDTO);
       }

        if(incorrectId.size() > 0){
           StringBuilder sb = new StringBuilder();
           incorrectId.forEach( i -> sb.append(i + " "));
           logger.info("Brak wiadomości zgodnych z id: " + sb.toString() + ".");
           throw new InfoException("Brak wiadomości zgodnych z id: " + sb.toString() + ".");
        }
    }
}
