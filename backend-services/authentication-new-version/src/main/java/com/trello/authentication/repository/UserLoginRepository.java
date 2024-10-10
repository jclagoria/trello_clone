package com.trello.authentication.repository;

import com.trello.authentication.model.UserLogin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserLoginRepository extends ReactiveCrudRepository<UserLogin, Long> {
    Mono<UserLogin> findByUserId(Long userId);
}
