package com.mwroblewski.service;

import com.mwroblewski.bean.Message;
import com.mwroblewski.bean.User;
import com.mwroblewski.exception.InfoException;
import com.mwroblewski.model.MessageDTO;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.MessageDTORepository;
import com.mwroblewski.repository.UserDTORepository;
import com.mwroblewski.validation.MessageValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageDTOService {

    @Autowired
    MessageDTORepository messageDTORepository;
    @Autowired
    UserDTORepository userDTORepository;
    @Autowired
    MessageValidationService messageValidationService;
    @Autowired
    ActiveDTOService activeDTOService;

    Logger logger = LoggerFactory.getLogger(MessageDTOService.class);

    private MessageDTO fromMessageToMessageDTO(Message message){
        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setText(message.getText().trim());
        messageDTO.setCreated(new Date());

        return messageDTO;
    }

    private Message fromMessageDTOToMessage(MessageDTO messageDTO){
        Message message = new Message();

        message.setId(messageDTO.getId());
        message.setText(messageDTO.getText());
        message.setCreated(messageDTO.getCreated());

        UserDTO userDTO = messageDTO.getFromUserDTO();
        message.setUser(addUserInfo(userDTO));

        return message;
    }

    private User addUserInfo(UserDTO userDTO){
        User user = new User();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());

        return user;
    }

    private MessageDTO findUserMessageDTOById(Set<MessageDTO> messageDTOS, Long id) {
        Optional<MessageDTO> optionalMessageDTO = messageDTOS
                                                    .stream()
                                                    .filter(m -> m.getId() == id)
                                                    .findFirst();

        if(optionalMessageDTO.isPresent())
            return optionalMessageDTO.get();

        return null;
    }

    private Set<MessageDTO> findUserMessagesDTOByUserId(Set<MessageDTO> messageDTOS, Long id) {
        Set<MessageDTO> tmpMessageDTOS = messageDTOS
                .stream()
                .filter(m -> m.getFromUserDTO().getId() == id)
                .collect(Collectors.toSet());

        return tmpMessageDTOS;
    }

    @Transactional
    public Set<Message> getUserAllMessages(String username){
        activeDTOService.updateActive(username);

        UserDTO userDTO = userDTORepository.findByUsername(username);
        Set<MessageDTO> messageDTOS = userDTO.getRecivedMessageDTOS();

        Set<Message> messages = new LinkedHashSet<>();
        for (MessageDTO m : messageDTOS) {
            messages.add(fromMessageDTOToMessage(m));
        }

        if(messages.size() > 0)
            return messages;
        else{
            logger.info("Użytkownik " + username + "nie posiada żadnej wiadomości.");
            throw new InfoException("Użytkownik " + username + " nie posiada żadnej wiadomości.");
        }
    }

    @Transactional
    public Set<Message> getUserAllMessagesByUserId(String username, Long id){
        activeDTOService.updateActive(username);

        UserDTO reciverUserDTO = userDTORepository.findByUsername(username);
        Set<MessageDTO> messageDTOS = reciverUserDTO.getRecivedMessageDTOS();
        if(messageDTOS.size() > 0){
            Set<MessageDTO> tmpMessageDTOS = findUserMessagesDTOByUserId(messageDTOS, id);
            Set<Message> tmpMessage = new LinkedHashSet<>();
            for (MessageDTO m : tmpMessageDTOS){
                tmpMessage.add(fromMessageDTOToMessage(m));
            }

            return tmpMessage;
        }

        logger.info("Użytkownik " + username + " nie posiada wiadomości zgodnej z id wysyłającego: " + id + ".");
        throw new InfoException("Użytkownik " + username + " nie posiada wiadomości zgodnej z id wysyłającego: " + id + ".");
    }

    @Transactional
    public void sendMessageToUserById(String username, Long id, Message message){
        activeDTOService.updateActive(username);

        UserDTO senderUserDTO = userDTORepository.findByUsername(username);
        UserDTO reciverUserDTO = userDTORepository.findById(id);
        if(reciverUserDTO == null){
            logger.info("Użytkownik zgodny z id: " + id + " nie istnieje.");
            throw new InfoException("Użytkownik zgodny z id: " + id + " nie istnieje.");
        }

        messageValidationService.checkMessageValidation(message);
        MessageDTO messageDTO = fromMessageToMessageDTO(message);
        messageDTO.setFromUserDTO(senderUserDTO);
        messageDTO.setToUserDTO(reciverUserDTO);

        senderUserDTO.getSentMessageDTOS().add(messageDTO);
        reciverUserDTO.getRecivedMessageDTOS().add(messageDTO);

        messageDTORepository.save(messageDTO);
    }

    @Transactional
    public void deleteUserMessageById(String username, Long id){
        activeDTOService.updateActive(username);

        UserDTO reciverUserDTO = userDTORepository.findByUsername(username);
        Set<MessageDTO> messageDTOS = reciverUserDTO.getRecivedMessageDTOS();

        MessageDTO messageDTO = findUserMessageDTOById(messageDTOS, id);
        if(messageDTO != null){
            UserDTO senderUserDTO = messageDTO.getFromUserDTO();

            senderUserDTO.getSentMessageDTOS().remove(messageDTO);
            reciverUserDTO.getRecivedMessageDTOS().remove(messageDTO);

            userDTORepository.save(senderUserDTO);
            userDTORepository.save(reciverUserDTO);
            messageDTORepository.delete(messageDTO);
        }
        else {
            logger.info("Użytkownik " + username + " nie posiada wiadomości zgodnej z id: " + id + ".");
            throw new InfoException("Użytkownik " + username + " nie posiada wiadomości zgodnej z id: " + id + ".");
        }
    }

    @Transactional
    public void deleteUserAllMessages(String username){
        activeDTOService.updateActive(username);

        UserDTO reciverUserDTO = userDTORepository.findByUsername(username);
        Set<MessageDTO> messageDTOS = new LinkedHashSet<>(reciverUserDTO.getRecivedMessageDTOS());

        if(messageDTOS.size() > 0){
            for(MessageDTO m : messageDTOS) {
                UserDTO senderUserDTO = m.getFromUserDTO();

                senderUserDTO.getSentMessageDTOS().remove(m);
                reciverUserDTO.getRecivedMessageDTOS().remove(m);

                userDTORepository.save(senderUserDTO);
                userDTORepository.save(reciverUserDTO);
            }
            messageDTORepository.delete(messageDTOS);
        }
    }
}
