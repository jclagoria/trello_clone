package ar.com.api.trello.authentication.handler;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import static org.junit.jupiter.api.Assertions.*;

class AccountHandlerTest {

    @Mock
    private AccountServiceImpl accountService;

    @InjectMocks
    private AccountHandler accountHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccountSuccess() {
        // Arrange
        Users createdUser = new Users();
        createdUser.setUsername("newuser");
        createdUser.setEmail("newuser@example.com");

        AccountCreationRequest requestDto = new AccountCreationRequest("newuser", "password123", "newuser@example.com");

        ServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(requestDto));  // Using MockServerRequest

        when(accountService.createAccount(any(String.class), any(String.class), any(String.class)))
                .thenReturn(Mono.just(createdUser));

        // Act
        Mono<ServerResponse> response = accountHandler.createAccount(serverRequest);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void createAccountFailure() {
        // Arrange
        AccountCreationRequest requestDto = new AccountCreationRequest("existinguser", "password123", "existinguser@example.com");

        ServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(requestDto));  // Using MockServerRequest

        when(accountService.createAccount(any(String.class), any(String.class), any(String.class)))
                .thenReturn(Mono.error(new RuntimeException("Username or email already exists")));

        // Act
        Mono<ServerResponse> response = accountHandler.createAccount(serverRequest);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

}