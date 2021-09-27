package com.example.appcraftmaster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String login;
    private String password;
    private String fio;
    private String phone;
}
