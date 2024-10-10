package com.trello.authentication.controller;

import com.trello.authentication.model.User;
import com.trello.authentication.service.impl.OAuth2AccountServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2LoginController {

    private final OAuth2AccountServiceImpl oAuth2AccountService;

    public OAuth2LoginController(OAuth2AccountServiceImpl oAuth2AccountService) {
        this.oAuth2AccountService = oAuth2AccountService;
    }

    @GetMapping("/success")
    public Mono<User> getOAuth2LoginInfo(Authentication authentication) {
        return oAuth2AccountService
                .processOAuth2User((OAuth2User) authentication.getPrincipal());
    }

}
