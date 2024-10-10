package ar.com.api.trello.authentication.repository;

import ar.com.api.trello.authentication.model.UsersLogin;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserLoginRepository extends R2dbcRepository<UsersLogin, Long> {
    Mono<UsersLogin> findByUserId(Long userId);
}
