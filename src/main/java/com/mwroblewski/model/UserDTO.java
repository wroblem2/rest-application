package com.mwroblewski.model;

import com.mwroblewski.common.Role;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class UserDTO {

    @Id
    @GeneratedValue
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // constructor
    public UserDTO() {
    }

    // relation with other tables
    @OneToOne(mappedBy = "userDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    ProfileDTO profileDTO;
    @OneToMany(mappedBy = "fromUserDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MessageDTO> sentMessageDTOS = new LinkedHashSet<>();
    @OneToMany(mappedBy = "toUserDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MessageDTO> recivedMessageDTOS = new LinkedHashSet<>();
    @OneToMany(mappedBy = "userDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OfferDTO> offerDTOS = new LinkedHashSet<>();
    @OneToMany(mappedBy = "userDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EntryDTO> entryDTOS = new LinkedHashSet<>();
    @OneToOne(mappedBy = "userDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ActiveDTO activeDTO;

    // getter/setter methods
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public ProfileDTO getProfileDTO() {
        return profileDTO;
    }
    public void setProfileDTO(ProfileDTO profileDTO) {
        this.profileDTO = profileDTO;
    }
    public Set<MessageDTO> getSentMessageDTOS() {
        return sentMessageDTOS;
    }
    public void setSentMessageDTOS(Set<MessageDTO> sentMessageDTOS) {
        this.sentMessageDTOS = sentMessageDTOS;
    }
    public Set<MessageDTO> getRecivedMessageDTOS() {
        return recivedMessageDTOS;
    }
    public void setRecivedMessageDTOS(Set<MessageDTO> recivedMessageDTOS) {
        this.recivedMessageDTOS = recivedMessageDTOS;
    }
    public Set<OfferDTO> getOfferDTOS() {
        return offerDTOS;
    }
    public void setOfferDTOS(Set<OfferDTO> offerDTOS) {
        this.offerDTOS = offerDTOS;
    }
    public Set<EntryDTO> getEntryDTOS() {
        return entryDTOS;
    }
    public void setEntryDTOS(Set<EntryDTO> entryDTOS) {
        this.entryDTOS = entryDTOS;
    }
    public ActiveDTO getActiveDTO() {
        return activeDTO;
    }
    public void setActiveDTO(ActiveDTO activeDTO) {
        this.activeDTO = activeDTO;
    }
}