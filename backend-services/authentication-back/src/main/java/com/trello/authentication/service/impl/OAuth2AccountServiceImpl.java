package com.trello.authentication.service.impl;

import com.trello.authentication.model.User;
import com.trello.authentication.repository.UserRepository;
import com.trello.authentication.service.OAuth2AccountService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OAuth2AccountServiceImpl implements OAuth2AccountService {

    private final UserRepository userRepository;

    public OAuth2AccountServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        return userRepository.findByUsername(email)
                .switchIfEmpty(userRepository
                        .save(User.builder()
                                .username(name)
                                .email(email)
                                .build())
                );
    }
}
