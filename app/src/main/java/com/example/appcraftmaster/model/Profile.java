package com.example.appcraftmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Profile {
    private Long id;
    private Category category;
    private Integer workExp;
    private String description;
}
