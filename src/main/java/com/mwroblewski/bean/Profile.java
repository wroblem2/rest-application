package com.mwroblewski.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mwroblewski.common.Contract;

import java.math.BigDecimal;
import java.util.Date;

public class Profile {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Europe/Warsaw")
    private Date born;
    private Contract contract;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String experiences;
    private String accomplishments;
    private String interests;
    private Attachment attachment;

    public Profile() {
    }
    public Profile(Long id, String name, String surname, String email, String phone, String address, Date born, Contract contract, BigDecimal minSalary, BigDecimal maxSalary, String experiences, String accomplishments, String interests) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.born = born;
        this.contract = contract;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.experiences = experiences;
        this.accomplishments = accomplishments;
        this.interests = interests;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getBorn() {
        return born;
    }
    public void setBorn(Date born) {
        this.born = born;
    }
    public Contract getContract() {
        return contract;
    }
    public void setContract(Contract contract) {
        this.contract = contract;
    }
    public BigDecimal getMinSalary() {
        return minSalary;
    }
    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }
    public BigDecimal getMaxSalary() {
        return maxSalary;
    }
    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }
    public String getExperiences() {
        return experiences;
    }
    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }
    public String getAccomplishments() {
        return accomplishments;
    }
    public void setAccomplishments(String accomplishments) {
        this.accomplishments = accomplishments;
    }
    public String getInterests() {
        return interests;
    }
    public void setInterests(String interests) {
        this.interests = interests;
    }
    public Attachment getAttachment() {
        return attachment;
    }
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
