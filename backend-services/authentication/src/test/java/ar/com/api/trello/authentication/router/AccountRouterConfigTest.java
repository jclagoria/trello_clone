package ar.com.api.trello.authentication.router;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.dto.LoginRequest;
import ar.com.api.trello.authentication.handler.AccountHandler;
import ar.com.api.trello.authentication.model.Users;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Instant;
import java.util.Map;

import static org.mockito.Mockito.*;

class AccountRouterConfigTest {

    @Mock
    private AccountHandler accountHandler;

    private static Faker dataFaker;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        RouterFunction<ServerResponse> routerFunction = new AccountRouterConfig()
                .accountRouter(accountHandler);
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        dataFaker = new Faker();
    }

    @Test
    @DisplayName("Should return s success creating account")
    void createAccountTest() {

        String username = dataFaker.internet().username();
        String email = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password(true);
        String expectedToken = dataFaker.internet().uuidv7();
        long idUser = dataFaker.barcode().ean8();
        Instant createdAt = Instant.now();

        // Arrange
        Users createdUser = new Users();
        createdUser.setId(idUser);
        createdUser.setUsername(username);
        createdUser.setEmail(email);
        createdUser.setCreatedAt(createdAt);
        createdUser.setUpdatedAt(null);

        // Mock the handler to return the expected response structure
        when(accountHandler.createAccount(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(
                        Map.of("token", expectedToken, "user", createdUser)
                ));

        webTestClient.post().uri("/api/service/account/create")
                .bodyValue(new AccountCreationRequest(
                        username,
                        password,
                        email))
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isEqualTo(expectedToken)
                .jsonPath("$.user.id").isEqualTo(idUser)
                .jsonPath("$.user.username").isEqualTo(username)
                .jsonPath("$.user.email").isEqualTo(email)
                .jsonPath("$.user.createdAt").isNotEmpty()
                .jsonPath("$.user.updatedAt").isEmpty();
    }

    @Test
    @DisplayName("Should return s error creating account")
    void createAccountFailure() {
        // Arrange
        when(accountHandler.createAccount(any())).thenReturn(ServerResponse.badRequest()
                .bodyValue("Username or email already exists"));

        // Act & Assert
        webTestClient.post()
                .uri("/api/service/account/create")
                .bodyValue(new AccountCreationRequest(
                        dataFaker.internet().username(),
                        dataFaker.internet().password(true),
                        dataFaker.internet().emailAddress()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Username or email already exists");
    }

    @Test
    @DisplayName("Should return a success intent of Login")
    void loginSuccessTest() {
        String email = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password(6, 8, true, false, true);
        String expectedToken = dataFaker.internet().uuidv7();
        long idUser = dataFaker.number().randomNumber();
        String username = dataFaker.internet().username();
        Instant createdAt = Instant.now();

        Users loginUser = new Users();
        loginUser.setId(idUser);
        loginUser.setUsername(username);
        loginUser.setEmail(email);
        loginUser.setCreatedAt(createdAt);

        when(accountHandler.login(any(ServerRequest.class)))
                .thenReturn(ServerResponse
                        .ok()
                        .bodyValue(
                           Map.of("token", expectedToken, "user", loginUser)
                        ));

        webTestClient.post().uri("/api/service/account/login")
                .bodyValue(LoginRequest.builder().email(email).password(password).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isEqualTo(expectedToken)
                .jsonPath("$.user.id").isEqualTo(idUser)
                .jsonPath("$.user.username").isEqualTo(username)
                .jsonPath("$.user.email").isEqualTo(email)
                .jsonPath("$.user.createdAt").isNotEmpty();
    }

    @Test
    @DisplayName("Should return an error for Invalid Credentials")
    void loginFailureTest() {
        when(accountHandler.login(any())).thenReturn(ServerResponse.badRequest()
                .bodyValue("Invalid credentials"));

        // Act & Assert
        webTestClient.post()
                .uri("/api/service/account/login")
                .bodyValue(new LoginRequest(
                        dataFaker.internet().emailAddress(),
                        dataFaker.internet().password()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Invalid credentials");
    }

}