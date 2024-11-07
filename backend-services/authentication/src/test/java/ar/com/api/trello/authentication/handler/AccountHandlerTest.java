package ar.com.api.trello.authentication.handler;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.dto.LoginRequest;
import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.security.JwtTokenProvider;
import ar.com.api.trello.authentication.services.impl.AccountServiceImpl;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

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
    @DisplayName("Should return a success creation of a user")
    void createAccountSuccess() {
        String username = dataFaker.internet().username();
        String password = dataFaker.internet().password(6, 8, true, false, true);  // Raw password
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
    @DisplayName("Should return an error to create an User")
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

    @Test
    @DisplayName("Should return a successful login response")
    void testLoginSuccess() {
        String email = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password(6, 8, true, false, true);  // Raw password
        String validToken = dataFaker.lorem().characters(45);

        Users loginUser = new Users();
        loginUser.setId(dataFaker.barcode().ean8());
        loginUser.setUsername(dataFaker.internet().username());
        loginUser.setEmail(email);

        LoginRequest requestDto = LoginRequest.builder().email(email).password(password).build();
        ServerRequest serverRequest = MockServerRequest.builder().body(Mono.just(requestDto));

        when(tokenProvider.createToken(any(String.class), any(String.class))).thenReturn(validToken);
        when(accountService.login(any(String.class), any(String.class)))
                .thenReturn(Mono.just(Map.of("token", validToken, "user", loginUser)));

        Mono<ServerResponse> expectedResponse = accountHandler.login(serverRequest);

        StepVerifier.create(expectedResponse)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return an error for failed login")
    void loginFailureInvalidCredentials() {
        String email = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password();

        LoginRequest requestDto = LoginRequest.builder().email(email).password(password).build();
        ServerRequest serverRequest = MockServerRequest.builder().body(Mono.just(requestDto));

        when(accountService.login(any(String.class), any(String.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        Mono<ServerResponse> expectedResponse = accountHandler.login(serverRequest);

        // Assert
        StepVerifier.create(expectedResponse)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }
}