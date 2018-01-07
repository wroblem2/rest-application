package com.mwroblewski.service;

import com.mwroblewski.bean.Active;
import com.mwroblewski.bean.User;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.ActiveDTO;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.ActiveDTORepository;
import com.mwroblewski.repository.UserDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ActiveDTOService {

    @Autowired
    ActiveDTORepository activeDTORepository;
    @Autowired
    UserDTORepository userDTORepository;

    Logger logger = LoggerFactory.getLogger(ActiveDTOService.class);

    private Active fromActiveDTOToActive(ActiveDTO activeDTO) {
        Active active = new Active();

        active.setId(activeDTO.getId());
        active.setSignInTime(activeDTO.getSignInTime());
        active.setActiveTime(activeDTO.getActiveTime());
        active.setSignOutTime(activeDTO.getSignOutTime());
        active.setSignInDate(activeDTO.getSignInDate());
        active.setActiveDate(activeDTO.getActiveDate());
        active.setSignOutDate(activeDTO.getSignOutDate());

        User user = new User();
        user.setId(activeDTO.getUserDTO().getId());
        user.setUsername(activeDTO.getUserDTO().getUsername());
        active.setUser(user);

        return active;
    }

    @Transactional
    public void updateActive(String username) {
        UserDTO userDTO = userDTORepository.findByUsername(username);

        ActiveDTO activeDTO = userDTO.getActiveDTO();
        if(activeDTO == null){
            activeDTO = new ActiveDTO();

            activeDTO.setUserDTO(userDTO);
            userDTO.setActiveDTO(activeDTO);
        }

        activeDTO.setActiveTime(new Date());
        activeDTO.setActiveDate(new Date());

        activeDTORepository.save(activeDTO);
    }

    @Transactional
    public void updateSignIn(String username) {
        UserDTO userDTO = userDTORepository.findByUsername(username);

        ActiveDTO activeDTO = userDTO.getActiveDTO();
        if(activeDTO == null){
            activeDTO = new ActiveDTO();

            activeDTO.setUserDTO(userDTO);
            userDTO.setActiveDTO(activeDTO);
        }

        activeDTO.setSignInTime(new Date());
        activeDTO.setSignInDate(new Date());


        activeDTORepository.save(activeDTO);
    }

    @Transactional
    public void updateSignOut(String username) {
        UserDTO userDTO = userDTORepository.findByUsername(username);

        ActiveDTO activeDTO = userDTO.getActiveDTO();
        if(activeDTO == null){
            activeDTO = new ActiveDTO();

            activeDTO.setUserDTO(userDTO);
            userDTO.setActiveDTO(activeDTO);
        }

        activeDTO.setSignOutTime(new Date());
        activeDTO.setSignOutDate(new Date());

        activeDTORepository.save(activeDTO);
    }

    @Transactional
    public List<Active> getAllActives(){
        List<ActiveDTO> activeDTOS = activeDTORepository.findAll();
        if(activeDTOS.size() > 0){
            List<Active> actives = new LinkedList<>();
            for(ActiveDTO a : activeDTOS){
                actives.add(fromActiveDTOToActive(a));

                return actives;
            }
        }

        logger.info("Brak aktywności do wyświetlenia");
        throw new InfoException("Bral aktywności do wyświetlenia.");
    }

    @Transactional
    public Active getUserActivesById(Long id){
        UserDTO userDTO = userDTORepository.findById(id);
        if(userDTO == null){
            logger.info("Użytkownik zgodny z id: " + id + " nie istnieje.");
            throw new InfoException("Użytkownik zgodny z id: " + id + " nie istnieje.");
        }

        ActiveDTO activeDTOS = userDTO.getActiveDTO();
        if(activeDTOS != null){
            Active active = fromActiveDTOToActive(activeDTOS);
            return active;
        }

        logger.info("Brak aktywności do wyświetlenia");
        throw new InfoException("Bral aktywności do wyświetlenia.");
    }
}
