package com.example.appcraftmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskStatusDto {
    private Long id;
    private Integer newStatus;
}
