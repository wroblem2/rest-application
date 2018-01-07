package com.mwroblewski;

import com.mwroblewski.common.Role;
import com.mwroblewski.model.UserDTO;
import com.mwroblewski.repository.UserDTORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    UserDTORepository userDTORepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("admin123");
        userDTO.setPassword(passwordEncoder.encode("admin123"));
        userDTO.setEnabled(true);
        userDTO.setRole(Role.ROLE_ADMIN);

        userDTORepository.save(userDTO);

        return;
    }
}