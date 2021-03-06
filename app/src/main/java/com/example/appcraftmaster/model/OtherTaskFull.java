package com.example.appcraftmaster.model;

import lombok.Data;

@Data
public class OtherTaskFull {
    private Long id;
    private String title;
    private String description;
    private String occupationName;
    private String createdAt;
    private CustomerInfo customer;
}
