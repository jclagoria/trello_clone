package ar.com.api.trello.authentication.handler;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.security.JwtTokenProvider;
import ar.com.api.trello.authentication.services.impl.AccountServiceImpl;
import net.datafaker.Faker;
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

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AccountHandler accountHandler;

    private static Faker dataFaker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataFaker = new Faker();
    }

    @Test
    void createAccountSuccess() {
        String username = dataFaker.internet().username();
        String password = dataFaker.internet().password();
        String email = dataFaker.internet().emailAddress();
        String jwtToken = dataFaker.internet().uuid();

        // Arrange
        Users createdUser = new Users();
        createdUser.setId(dataFaker.barcode().ean8());
        createdUser.setUsername(username);
        createdUser.setEmail(email);

        AccountCreationRequest requestDto = new AccountCreationRequest(username, password, email);

        ServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(requestDto));  // Using MockServerRequest

        when(accountService.createAccount(any(String.class), any(String.class), any(String.class)))
                .thenReturn(Mono.just(createdUser));
        when(tokenProvider.createToken(createdUser.getUsername(), String.valueOf(createdUser.getId())))
                .thenReturn(jwtToken);

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
        AccountCreationRequest requestDto = new AccountCreationRequest(
                dataFaker.internet().username(),
                dataFaker.internet().password(),
                dataFaker.internet().emailAddress());

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