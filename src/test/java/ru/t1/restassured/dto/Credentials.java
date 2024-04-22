package ru.t1.restassured.dto;

import lombok.Data;

@Data
public class Credentials {
    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
