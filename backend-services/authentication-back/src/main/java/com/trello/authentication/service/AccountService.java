package com.trello.authentication.service;

import com.trello.authentication.model.User;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<User> createAccount(String username, String email, String password);
}
