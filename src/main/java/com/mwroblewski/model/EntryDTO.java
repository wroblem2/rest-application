package com.mwroblewski.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ENTRIES")
public class EntryDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Europe/Warsaw")
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date applied;
    @Column(columnDefinition = "VARCHAR(250)")
    private String comment;

    // relation with other tables
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDTO userDTO;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private OfferDTO offerDTO;

    // constructors
    public EntryDTO() {
    }

    // getter/setter methods
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

    public UserDTO getUserDTO() {
        return userDTO;
    }
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
    public OfferDTO getOfferDTO() {
        return offerDTO;
    }
    public void setOfferDTO(OfferDTO offerDTO) {
        this.offerDTO = offerDTO;
    }
}