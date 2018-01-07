package com.mwroblewski.service;

import com.mwroblewski.bean.Profile;
import com.mwroblewski.bean.User;
import com.mwroblewski.common.Role;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.UserDTORepository;
import com.mwroblewski.validation.ProfileValidationService;
import com.mwroblewski.validation.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDTOService {

    @Autowired
    UserDTORepository userDTORepository;
    @Autowired
    ProfileDTOService profileDTOService;
    @Autowired
    UserValidationService userValidationService;
    @Autowired
    ActiveDTOService activeDTOService;

    @Autowired
    ProfileValidationService profileValidationService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserDTOService.class);

    public UserDTO fromUserToUserDTO(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(user.getUsername().trim());
        String hashPassword = passwordEncoder.encode(user.getPassword().trim());
        userDTO.setPassword(hashPassword);
        userDTO.setEnabled(true);
        userDTO.setRole(Role.ROLE_USER);

        return userDTO;
    }

    public User formUserDTOToUser(UserDTO userDTO){
        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());

        return user;
    }

    private User fromUserDTOToUser(UserDTO userDTO){
        User user = new User(userDTO.getId(), userDTO.getUsername());

        return user;
    }

    @Transactional
    public void updateUser(String username, Long id, User user){
        activeDTOService.updateActive(username);

        userValidationService.checkUserValidation(user);

        UserDTO userDTO = userDTORepository.findById(id);
        if(userDTO == null){
            logger.info("Użytkownik zgodny z id: " + id + " nie istnieje.");
            throw new InfoException("Użytkownik zgodny z id: " + id + " nie istnieje.");
        }

        if(user.getUsername() != null)
            userDTO.setUsername(user.getUsername());
        if(user.getPassword() != null)
            userDTO.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        if(user.getRole() != null) {
            UserDTO admin = userDTORepository.findByUsername(username);
            if (admin.getRole().equals(Role.ROLE_ADMIN) && user.getRole() != null)
                userDTO.setRole(user.getRole());
            else {
                logger.info("Zmiana uprawnień użytkownika zgodnego z id: " + id + " możliwa tylko dla admina.");
                throw new InfoException("Zmiana uprawnień użytkownika zgodnego z id: " + id + " możliwa tylko dla admina.");
            }
        }
        userDTORepository.save(userDTO);
    }

    @Transactional
    public void addUser(String username, User user){
        userValidationService.checkUserValidation(user);

        UserDTO userDTO = fromUserToUserDTO(user);
        UserDTO admin = userDTORepository.findByUsername(username);
        if(admin.getRole().equals(Role.ROLE_ADMIN) && user.getRole() != null){
            userDTO.setRole(user.getRole());
        }

        userDTORepository.save(userDTO);
    }

    @Transactional
    public void regUser(User user) {
        userValidationService.checkUserValidation(user);

        UserDTO userDTO = fromUserToUserDTO(user);
        userDTORepository.save(userDTO);
    }

    @Transactional
    public void regUserWithProfile(User user, Profile profile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        userValidationService.checkUserValidation(user);
        profileValidationService.checkProfileAddValidation(profile);

        UserDTO userDTO = fromUserToUserDTO(user);
        userDTORepository.save(userDTO);
        profileDTOService.addProfileWithoutValidation(user.getUsername(), profile);
    }

    @Transactional
    public List<User> searchUsers(User user){
        List<UserDTO> tmpUserDTOS = userDTORepository.findAll();
        List<User> users = new ArrayList<>();
        if(tmpUserDTOS.size() > 0){
            for(UserDTO u : tmpUserDTOS)
                users.add(fromUserDTOToUser(u));

        }
        else{
            logger.info("Brak użytkowników.");
            throw new InfoException("Brak użytkowników.");
        }

        if(user.getId() != null) {
            users = users.stream()
                    .filter(u -> u.getId() == user.getId())
                    .collect(Collectors.toList());
        }
        if(user.getUsername() != null) {
            users = users.stream()
                    .filter(u -> u.getUsername() == user.getUsername())
                    .collect(Collectors.toList());
        }
        if(user.getRole() != null) {
            users = users.stream()
                    .filter(u -> u.getRole() == user.getRole())
                    .collect(Collectors.toList());
        }

        if(users.size() == 0){
            logger.info("Brak użytkowników.");
            throw new InfoException("Brak użytkowników.");
        }

        return users;
    }
}

