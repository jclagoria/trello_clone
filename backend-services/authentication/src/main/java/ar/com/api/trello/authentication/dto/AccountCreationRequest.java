package ar.com.api.trello.authentication.dto;

import lombok.Getter;

@Getter
public class AccountCreationRequest {

    private String username;
    private String password;
    private String email;

}
