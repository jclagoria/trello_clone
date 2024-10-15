package ar.com.api.trello.authentication.router;

import ar.com.api.trello.authentication.dto.AccountCreationRequest;
import ar.com.api.trello.authentication.handler.AccountHandler;
import ar.com.api.trello.authentication.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.Mockito.*;

class AccountRouterConfigTest {

    @Mock
    private AccountHandler accountHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        RouterFunction<ServerResponse> routerFunction = new AccountRouterConfig()
                .accountRouter(accountHandler);
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void createAccountTest() {
        // Arrange
        Users createdUser = new Users();
        createdUser.setUsername("newuser");
        createdUser.setEmail("newuser@example.com");

        when(accountHandler.createAccount(any()))
                .thenReturn(ServerResponse.ok().bodyValue(createdUser));

        webTestClient.post().uri("/api/service/account/create")
                .bodyValue(new AccountCreationRequest(
                        "newuser",
                        "password123",
                        "newuser@example.com"))
                .exchange().expectStatus().isOk()
                .expectBody(Users.class)
                .consumeWith(response -> {
                    Users user = response.getResponseBody();
                    assert user != null;
                    assert "newuser".equals(user.getUsername());
                    assert "newuser@example.com".equals(user.getEmail());
                });
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
                        "existinguser",
                        "password123",
                        "existinguser@example.com"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Username or email already exists");
    }

}