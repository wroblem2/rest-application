package com.mwroblewski.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ACTIVITIES")
public class ActiveDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIME)
    private Date signInTime;
    @Temporal(TemporalType.TIME)
    private Date activeTime;
    @Temporal(value = TemporalType.TIME)
    private Date signOutTime;
    @Temporal(TemporalType.DATE)
    private Date signInDate;
    @Temporal(TemporalType.DATE)
    private Date activeDate;
    @Temporal(value = TemporalType.DATE)
    private Date signOutDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDTO userDTO;

    public ActiveDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getSignInTime() {
        return signInTime;
    }
    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }
    public Date getActiveTime() {
        return activeTime;
    }
    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }
    public Date getSignOutTime() {
        return signOutTime;
    }
    public void setSignOutTime(Date signOutTime) {
        this.signOutTime = signOutTime;
    }
    public Date getSignInDate() {
        return signInDate;
    }
    public void setSignInDate(Date signInDate) {
        this.signInDate = signInDate;
    }
    public Date getActiveDate() {
        return activeDate;
    }
    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }
    public Date getSignOutDate() {
        return signOutDate;
    }
    public void setSignOutDate(Date signOutDate) {
        this.signOutDate = signOutDate;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
