package com.mwroblewski.bean;

import java.util.Date;

public class Entry {

    private Long id;
    private Date applied;
    private String comment;
    private User user;
    private Offer offer;

    public Entry() {
    }
    public Entry(Long id, Date applied, String comment, User user, Offer offer) {
        this.id = id;
        this.applied = applied;
        this.comment = comment;
        this.user = user;
        this.offer = offer;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getApplied() {
        return applied;
    }
    public void setApplied(Date applied) {
        this.applied = applied;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Offer getOffer() {
        return offer;
    }
    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
