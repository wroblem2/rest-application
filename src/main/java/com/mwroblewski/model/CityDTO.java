package com.mwroblewski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CITIES")
@JsonIgnoreProperties(value = "offers")
public class CityDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String name;

    // relations with other tables
    @OneToMany(mappedBy = "cityDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OfferDTO> offerDTOS;

    // constructors
    public CityDTO() {
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
    public List<OfferDTO> getOfferDTOS() {
        return offerDTOS;
    }
    public void setOfferDTOS(List<OfferDTO> offerDTOS) {
        this.offerDTOS = offerDTOS;
    }
}
