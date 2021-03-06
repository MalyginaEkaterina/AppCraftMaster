package com.example.appcraftmaster.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDto {
    private Integer category;
    private Integer workExp;
    private String description;

    public ProfileDto(Profile profile) {
        this.category = profile.getOccupation().getId();
        this.workExp = profile.getWorkExp();
        this.description = profile.getDescription();
    }
}
