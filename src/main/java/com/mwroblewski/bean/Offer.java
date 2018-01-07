package com.mwroblewski.bean;

import com.mwroblewski.common.Contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Offer {

    private Long id;
    private String name;
    private String description;
    private Date published;
    private Contract contract;
    private BigDecimal maxSalary;
    private BigDecimal minSalary;
    private Set<Technology> technologies = new LinkedHashSet<>();
    private Set<Entry> entries = new LinkedHashSet<>();
    private City city;
    private User user;

    public Offer() {
    }
    public Offer(Long id, String name, String description, Date published, Contract contract, BigDecimal maxSalary, BigDecimal minSalary, Set<Technology> technologies, Set<Entry> entries, City city, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.published = published;
        this.contract = contract;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.technologies = technologies;
        this.entries = entries;
        this.city = city;
        this.user = user;
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
    public Set<Technology> getTechnologies() {
        return technologies;
    }
    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }
    public Set<Entry> getEntries() {
        return entries;
    }
    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }
    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
