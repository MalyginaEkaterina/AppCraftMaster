package com.example.appcraftmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    private String title;
    private String description;
    private Integer serviceId;
}
