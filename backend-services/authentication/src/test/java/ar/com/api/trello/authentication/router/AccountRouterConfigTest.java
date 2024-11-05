package ar.com.api.trello.authentication.router;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.handler.AccountHandler;
import ar.com.api.trello.authentication.model.Users;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

}