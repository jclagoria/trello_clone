package com.trello.authentication.service;

import com.trello.authentication.model.RefreshTokens;
import reactor.core.publisher.Mono;

public interface RefreshTokenService {

    Mono<RefreshTokens> createRefreshToken (Long userId);
    Mono<Boolean> isRefreshTokenValid(String token);
    Mono<Void> deleteRefreshToken(String token);

}
