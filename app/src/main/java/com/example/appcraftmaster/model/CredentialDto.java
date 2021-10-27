package com.example.appcraftmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialDto {
    private String code;
    private String value;
    private String name;
}
