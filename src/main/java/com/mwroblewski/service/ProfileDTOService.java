package com.mwroblewski.service;

import com.mwroblewski.bean.Attachment;
import com.mwroblewski.bean.Profile;
import com.mwroblewski.bean.User;
import com.mwroblewski.exception.GeneralException;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.AttachmentDTO;
import com.mwroblewski.model.ProfileDTO;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.ProfileDTORepository;
import com.mwroblewski.repository.UserDTORepository;
import com.mwroblewski.validation.ProfileValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProfileDTOService {

    @Autowired
    ProfileDTORepository profileDTORepository;
    @Autowired
    UserDTORepository userDTORepository;
    @Autowired
    AttachmentDTOService attachmentDTOService;
    @Autowired
    ActiveDTOService activeDTOService;
    @Autowired
    ProfileValidationService profileValidationService;

    Logger logger = LoggerFactory.getLogger(ProfileDTOService.class);

    private ProfileDTO fromProfileToProfileDTO(Profile profile) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ProfileDTO profileDTO = new ProfileDTO();

        Field[] fieldProfile = profile.getClass().getDeclaredFields();
        for(int i = 0; i < fieldProfile.length; i++){
            if(fieldProfile[i].getName().equals("attachment"))
                continue;

            String methodSufix = fieldProfile[i].getName().toUpperCase().charAt(0) + fieldProfile[i].getName().substring(1);
            Method setMethod = profileDTO.getClass().getDeclaredMethod("set" + methodSufix, fieldProfile[i].getType());
            Method getMethod = profile.getClass().getDeclaredMethod("get" + methodSufix);

            if(getMethod.invoke(profile) != null){
                if(fieldProfile[i].getType() == String.class)
                    setMethod.invoke(profileDTO, ((String) getMethod.invoke(profile)).trim());
                else
                    setMethod.invoke(profileDTO, getMethod.invoke(profile));
            }
        }

        if(profile.getAttachment() != null) {
            AttachmentDTO attachmentDTO = attachmentDTOService.fromAttachmentToAttachmentDTO(profile.getAttachment());
            attachmentDTO.setProfileDTO(profileDTO);
            profileDTO.setAttachmentDTO(attachmentDTO);
        }

        return profileDTO;
    }

    private Profile fromProfileDTOToProfile(ProfileDTO profileDTO) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Profile profile = new Profile();

        Field[] fieldProfileDTO = profileDTO.getClass().getDeclaredFields();
        for(int i = 0; i < fieldProfileDTO.length; i++){
            if(fieldProfileDTO[i].getName().equals("attachmentDTO") || fieldProfileDTO[i].getName().equals("userDTO"))
                continue;

            String methodSufix = fieldProfileDTO[i].getName().toUpperCase().charAt(0) + fieldProfileDTO[i].getName().substring(1);
            Method setMethod = profile.getClass().getDeclaredMethod("set" + methodSufix, fieldProfileDTO[i].getType());
            Method getMethod = profileDTO.getClass().getDeclaredMethod("get" + methodSufix);

            setMethod.invoke(profile, getMethod.invoke(profileDTO));
        }

        if(profileDTO.getAttachmentDTO() != null) {
            Attachment attachment = attachmentDTOService.fromAttachmentDTOToAttachment(profileDTO.getAttachmentDTO());
            profile.setAttachment(attachment);
        }

        return profile;
    }

    @Transactional
    public List<Profile> getAllProfiles() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<ProfileDTO> profileDTOS = profileDTORepository.findAll();

        if(profileDTOS.size() > 0) {
            List<Profile> profiles = new LinkedList<>();
            for (ProfileDTO p : profileDTOS) {
                profiles.add(fromProfileDTOToProfile(p));
            }

            return profiles;
        }

        logger.info("Brak profili użytkowników.");
        throw new InfoException("Brak profili użytkowników.");
    }

    @Transactional
    public Profile getUserProfile(String username) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        ProfileDTO profileDTO = userDTO.getProfileDTO();

        if(profileDTO != null) {
            Profile profile = fromProfileDTOToProfile(profileDTO);
            return profile;
        }

        logger.info("Użytkownik " + username + "nie posiada profilu.");
        throw new InfoException("Użytkownik " + username + "nie posiada profilu.");

    }

    @Transactional
    public Profile getUserProfileById(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        UserDTO userDTO = userDTORepository.findById(id);
        if(userDTO == null){
            logger.info("Użytkownik zgodny z id: " + id + " nie istnieje.");
            throw new InfoException("Użytkownik zgodny z id: " + id + " nie istnieje.");
        }

        ProfileDTO profileDTO = userDTO.getProfileDTO();
        if(profileDTO != null){
            Profile profile = fromProfileDTOToProfile(profileDTO);
            return profile;
        }

        logger.info("Użytkownik " + userDTO.getUsername() + " nie posiada profilu.");
        throw new InfoException("Użytkownik " + userDTO.getUsername() + " nie posiada profilu.");

    }

    @Transactional
    public void updateUserProfile(String username, Profile profile) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        activeDTOService.updateActive(username);

        profileValidationService.checkProfileUpdateValidation(profile);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        ProfileDTO profileDTO = userDTO.getProfileDTO();

        if(profileDTO == null) {
            logger.info("Użytkownik " + username + " nie posiada profilu.");
            throw new InfoException("Użytkownik "+ username + " nie posiada profilu.");
        }

        Field[] fieldProfile = profile.getClass().getDeclaredFields();
        for(int i = 0; i < fieldProfile.length; i++){
            if(fieldProfile[i].getName().equals("attachment"))
                continue;

            String methodSufix = fieldProfile[i].getName().toUpperCase().charAt(0) + fieldProfile[i].getName().substring(1);
            Method setMethod = profileDTO.getClass().getDeclaredMethod("set" + methodSufix, fieldProfile[i].getType());
            Method getMethod = profile.getClass().getDeclaredMethod("get" + methodSufix);

            if(getMethod.invoke(profile) != null){
                if(fieldProfile[i].getType() == String.class)
                    setMethod.invoke(profileDTO, ((String) getMethod.invoke(profile)).trim());
                else
                    setMethod.invoke(profileDTO, getMethod.invoke(profile));
            }
        }

        if(profile.getAttachment() != null) {
            AttachmentDTO attachmentDTO = attachmentDTOService.fromAttachmentToAttachmentDTO(profile.getAttachment());
            attachmentDTO.setProfileDTO(profileDTO);
            profileDTO.setAttachmentDTO(attachmentDTO);
        }

        userDTORepository.save(userDTO);
    }

    @Transactional
    public void addProfile(String username, Profile profile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        activeDTOService.updateActive(username);

        profileValidationService.checkProfileAddValidation(profile);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        ProfileDTO profileDTO = fromProfileToProfileDTO(profile);

        profileDTO.setUserDTO(userDTO);
        userDTO.setProfileDTO(profileDTO);

        userDTORepository.save(userDTO);
    }

    @Transactional
    public void addProfileWithoutValidation(String username, Profile profile) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        ProfileDTO profileDTO = fromProfileToProfileDTO(profile);

        profileDTO.setUserDTO(userDTO);
        userDTO.setProfileDTO(profileDTO);

        userDTORepository.save(userDTO);
    }
}
