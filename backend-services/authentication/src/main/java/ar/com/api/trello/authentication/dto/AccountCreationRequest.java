package ar.com.api.trello.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationRequest {

    private String username;
    private String password;
    private String email;

}
