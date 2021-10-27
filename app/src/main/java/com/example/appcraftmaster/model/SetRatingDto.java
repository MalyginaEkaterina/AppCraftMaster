package com.example.appcraftmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetRatingDto {
    private Long bidId;
    private Float rating;
}
