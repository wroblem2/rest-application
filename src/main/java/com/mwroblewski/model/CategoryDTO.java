package com.mwroblewski.model;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORIES")
public class CategoryDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
    private String name;

    // constructors
    public CategoryDTO() {
    }
    public CategoryDTO(String name) {
        this.name = name;
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
}
