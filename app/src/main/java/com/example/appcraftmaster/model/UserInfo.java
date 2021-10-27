package com.example.appcraftmaster.model;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
    private String login;
    private String name;
    private Float rating;
    private Integer numRatings;
    private List<Credential> credentials;

    public void setValue(UserInfo value) {
        this.login = value.getLogin();
        this.name = value.getName();
        this.credentials = value.getCredentials();
    }
}
