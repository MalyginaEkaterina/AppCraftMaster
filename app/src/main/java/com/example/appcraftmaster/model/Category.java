package com.example.appcraftmaster.model;

import java.util.List;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private String name;
    private List<Category> child;
}
