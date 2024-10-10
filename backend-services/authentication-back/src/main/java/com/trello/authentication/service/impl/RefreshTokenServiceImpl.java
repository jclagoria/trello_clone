package com.trello.authentication.service.impl;

import com.trello.authentication.model.RefreshTokens;
import com.trello.authentication.repository.RefreshTokenRepository;
import com.trello.authentication.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

   private final RefreshTokenRepository refreshTokenRepository;

   public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
       this.refreshTokenRepository = refreshTokenRepository;
   }

    @Override
    public Mono<RefreshTokens> createRefreshToken(Long userId) {
       RefreshTokens refreshTokens = new RefreshTokens();
       refreshTokens.setUserId(userId);
       refreshTokens.setToken(UUID.randomUUID().toString());
       refreshTokens.setExpiresAt(Instant.now().plus(Duration.ofDays(7)));

        return refreshTokenRepository.save(refreshTokens);
    }

    @Override
    public Mono<Boolean> isRefreshTokenValid(String token) {
        return refreshTokenRepository
                .findByToken(token)
                .map(refreshToken -> refreshToken.getExpiresAt().isAfter(Instant.now()));
    }

    @Override
    public Mono<Void> deleteRefreshToken(String token) {
        return refreshTokenRepository.deleteByToken(token);
    }
}
