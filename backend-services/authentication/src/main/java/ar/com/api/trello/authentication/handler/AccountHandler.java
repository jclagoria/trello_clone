package ar.com.api.trello.authentication.handler;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.dto.ErrorResponse;
import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.security.JwtTokenProvider;
import ar.com.api.trello.authentication.services.impl.AccountServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AccountHandler {

    private final AccountServiceImpl accountService;
    private final JwtTokenProvider jwtTokenProvider;

    public AccountHandler(AccountServiceImpl accountService, JwtTokenProvider jwtTokenProvider) {
        this.accountService = accountService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(AccountCreationRequest.class)
                .flatMap(body -> accountService
                        .createAccount(body.getUsername(), body.getPassword(), body.getEmail())).
                cast(Users.class)
                .flatMap(user -> {
                    String token = jwtTokenProvider.createToken(
                            user.getUsername(),
                            String.valueOf(user.getId())
                    );
                    return ServerResponse.ok().bodyValue(Map.of("token", token, "user", user));
                })
                .onErrorResume(e -> {
                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .code(401)
                            .message("Error creating account: " + e.getMessage())
                            .build();
                    return ServerResponse.badRequest().bodyValue(errorResponse);
                });
    }
}
