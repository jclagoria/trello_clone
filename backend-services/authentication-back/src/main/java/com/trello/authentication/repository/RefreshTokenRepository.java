package com.trello.authentication.repository;

import com.trello.authentication.model.RefreshTokens;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshTokens, String> {
    Mono<RefreshTokens> findByToken(String token);
    Mono<Void> deleteByToken(String token);
}
