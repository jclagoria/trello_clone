package com.trello.authentication.dto;

import lombok.Getter;

@Getter
public class NewUserRequest {
    private String username;
    private String passwordHash;
    private String email;
}
