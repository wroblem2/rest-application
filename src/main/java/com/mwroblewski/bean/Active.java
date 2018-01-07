package com.mwroblewski.bean;

import java.util.Date;

public class Active {

    private Long id;
    private Date signInTime;
    private Date activeTime;
    private Date signOutTime;
    private Date signInDate;
    private Date activeDate;
    private Date signOutDate;
    private User user;

    public Active() {
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
