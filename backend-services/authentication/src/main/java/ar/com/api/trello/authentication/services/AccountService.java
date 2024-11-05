package ar.com.api.trello.authentication.services;

import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<Object> createAccount(String username, String password, String email);
    Mono<Object> login(String email, String password);
}
