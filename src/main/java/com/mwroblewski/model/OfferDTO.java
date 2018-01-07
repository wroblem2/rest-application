package com.mwroblewski.model;

import com.mwroblewski.common.Contract;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "OFFERS")
public class OfferDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String name;
    @Column(columnDefinition = "VARCHAR(5000)", nullable = false)
    private String description;
    @Column(nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date published;
    @Enumerated(value = EnumType.STRING)
    private Contract contract;
    @Digits(integer = 6, fraction = 2)
    private BigDecimal maxSalary;
    @Digits(integer = 6, fraction = 2)
    private BigDecimal minSalary;

    // relations with other tables
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDTO userDTO;
    @OneToMany(mappedBy = "offerDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TechnologyDTO> technologies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "offerDTO", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EntryDTO> entryDTOS = new LinkedHashSet<>();
    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityDTO cityDTO;

    // constructors
    public OfferDTO() {
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getPublished() {
        return published;
    }
    public void setPublished(Date published) {
        this.published = published;
    }
    public Contract getContract() {
        return contract;
    }
    public void setContract(Contract contract) {
        this.contract = contract;
    }
    public BigDecimal getMaxSalary() {
        return maxSalary;
    }
    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }
    public BigDecimal getMinSalary() {
        return minSalary;
    }
    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
    public Set<TechnologyDTO> getTechnologies() {
            return technologies;
    }
    public void setTechnologies(Set<TechnologyDTO> technologies) {
        this.technologies = technologies;
    }
    public Set<EntryDTO> getEntryDTOS() {
        return entryDTOS;
    }
    public void setEntryDTOS(Set<EntryDTO> entryDTOS) {
        this.entryDTOS = entryDTOS;
    }
    public CityDTO getCityDTO() {
        return cityDTO;
    }
    public void setCityDTO(CityDTO cityDTO) {
        this.cityDTO = cityDTO;
    }
}
