package com.trello.authentication.controller;

import com.trello.authentication.dto.NewUserRequest;
import com.trello.authentication.model.User;
import com.trello.authentication.service.impl.AccountServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public Mono<User> createAccount(@RequestBody NewUserRequest newUserRequest) {
        return accountService.createAccount(newUserRequest.getUsername(),
                newUserRequest.getEmail(),
                newUserRequest.getPasswordHash());
    }

}
