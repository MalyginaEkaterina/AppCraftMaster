package com.example.appcraftmaster.model;

import lombok.Data;

@Data
public class Occupation {
    private Integer id;
    private String name;

    public Occupation(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
