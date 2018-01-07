package com.mwroblewski.bean;

import com.mwroblewski.common.Level;

public class Technology {

    private Long id;
    private Level level;
    private Category category;

    public Technology() {
    }
    public Technology(Long id, Level level, Category category) {
        this.id = id;
        this.level = level;
        this.category = category;
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
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
