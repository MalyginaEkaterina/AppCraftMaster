package com.example.appcraftmaster.model;

import lombok.Data;

@Data
public class TaskFull {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String createdAt;
    private Integer status;
    private Response acceptedResponse;
}
