package com.trello.authentication.controller;

import com.trello.authentication.model.User;
import com.trello.authentication.service.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(AccountController.class)
public class AccountControllerTest {

    private final WebTestClient webTestClient;
    private final AccountServiceImpl accountService;

    public AccountControllerTest(WebTestClient webTestClient, AccountServiceImpl accountService) {
        this.webTestClient = webTestClient;
        this.accountService = accountService;
    }

    @Test
    void testCreateAccount() {
        User user = User.builder().id(1L).username("testuser").email("email@gamil.com").build();

        when(accountService
                .createAccount(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(user));

        webTestClient.post().uri("/api/account/create")
                .bodyValue(new User())
                .exchange().expectStatus().isOk()
                .expectBody();
    }

}
