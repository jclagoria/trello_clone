package ar.com.api.trello.authentication.handler;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.services.impl.AccountServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {

    private final AccountServiceImpl accountService;

    public AccountHandler(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(AccountCreationRequest.class)
                .flatMap(body -> accountService
                        .createAccount(body.getUsername(), body.getPassword(), body.getEmail()))
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .onErrorResume(e -> ServerResponse
                        .badRequest().bodyValue("Error creating account: " + e.getMessage()));
    }
}
