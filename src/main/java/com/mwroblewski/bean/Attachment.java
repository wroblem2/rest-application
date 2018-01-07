package com.mwroblewski.bean;

public class Attachment {

    private Long id;
    private byte[] photo;
    private byte[] cv;
    private byte[] coverLetter;

    public Attachment() {
    }
    public Attachment(Long id, byte[] photo, byte[] cv, byte[] coverLetter) {
        this.id = id;
        this.photo = photo;
        this.cv = cv;
        this.coverLetter = coverLetter;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public byte[] getPhoto() {
        return photo;
    }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    public byte[] getCv() {
        return cv;
    }
    public void setCv(byte[] cv) {
        this.cv = cv;
    }
    public byte[] getCoverLetter() {
        return coverLetter;
    }
    public void setCoverLetter(byte[] coverLetter) {
        this.coverLetter = coverLetter;
    }
}
