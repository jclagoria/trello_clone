package com.trello.authentication.controller;

import com.trello.authentication.dto.NewAccount;
import com.trello.authentication.model.User;
import com.trello.authentication.service.impl.AccountServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    @ResponseBody
    public Mono<User> createAccount(@RequestBody NewAccount newAccount) {
        return accountService.createAccount(newAccount.getUsername(), newAccount.getEmail(), newAccount.getPassword());
    }

    @GetMapping("/hola")
    @ResponseBody
    public Mono<ServerResponse> alo() {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Hello, Spring!"));
    }
}
