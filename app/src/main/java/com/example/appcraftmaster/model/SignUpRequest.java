package com.example.appcraftmaster.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String login;
    private String name;
    private String password;
    private List<CredentialDto> credentialDtos;
}
