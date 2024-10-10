package com.trello.authentication.service;

import com.trello.authentication.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import reactor.core.publisher.Mono;

public interface OAuth2AccountService {
    Mono<User> processOAuth2User(OAuth2User oAuth2User);
}
