package com.example.appcraftmaster.model;

import lombok.Data;

@Data
public class AddProfile {
    private Integer occupationId;
    private Integer workExp;
    private String description;

    public AddProfile(Profile profile) {
        this.occupationId = profile.getOccupation().getId();
        this.workExp = profile.getWorkExp();
        this.description = profile.getDescription();
    }
}
