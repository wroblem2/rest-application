package com.mwroblewski.model;

import com.mwroblewski.common.Contract;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PROFILES")
public class ProfileDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;
    @Column(columnDefinition = "VARCHAR(50)", nullable = false)
    private String surname;
    @Column(columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;
    @Column(columnDefinition = "VARCHAR(20)")
    private String phone;
    @Column(columnDefinition = "VARCHAR(50)")
    private String address;
    @Temporal(value = TemporalType.DATE)
    private Date born;

    @Enumerated(value = EnumType.STRING)
    private Contract contract;
    @Digits(integer = 6, fraction = 2)
    private BigDecimal minSalary;
    @Digits(integer = 6, fraction = 2)
    private BigDecimal maxSalary;

    @Column(columnDefinition = "VARCHAR(2000)")
    private String experiences;
    @Column(columnDefinition = "VARCHAR(2000)")
    private String accomplishments;
    @Column(columnDefinition = "VARCHAR(2000)")
    private String interests;

    // relation with other tables
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDTO userDTO;
    @OneToOne(mappedBy = "profileDTO", cascade = CascadeType.ALL)
    private AttachmentDTO attachmentDTO;

    // constructors
    public ProfileDTO() {
    }

    // getter/setter methods
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

    public UserDTO getUserDTO() {
        return userDTO;
    }
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
    public AttachmentDTO getAttachmentDTO() {
        return attachmentDTO;
    }
    public void setAttachmentDTO(AttachmentDTO attachmentDTO) {
        this.attachmentDTO = attachmentDTO;
    }
}
