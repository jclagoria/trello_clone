package ar.com.api.trello.authentication.services;

import reactor.core.publisher.Mono;

public interface AccountService {

    public Mono<Object> createAccount(String username, String password, String email);

}
