package com.example.appcraftmaster.model;

import lombok.Data;

@Data
public class MyExecTask {
    private Long id;
    private String title;
    private String description;
    private String occupationName;
    private String createdAt;
    private Integer status;
    private CustomerInfo customer;
    private Response acceptedBid;
}
