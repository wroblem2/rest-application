package com.mwroblewski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwroblewski.bean.Offer;
import com.mwroblewski.common.Level;

import javax.persistence.*;

@Entity
@Table(name = "TECHNOLOGY")
@JsonIgnoreProperties(value = {"offer"})
public class TechnologyDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Level level;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private CategoryDTO categoryDTO;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private OfferDTO offerDTO;

    public TechnologyDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }
    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }
    public OfferDTO getOfferDTO() {
        return offerDTO;
    }
    public void setOfferDTO(OfferDTO offerDTO) {
        this.offerDTO = offerDTO;
    }
}
