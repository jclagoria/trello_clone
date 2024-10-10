package ar.com.api.trello.authentication.repository;

import ar.com.api.trello.authentication.model.Users;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UsersRepository extends R2dbcRepository<Users, Long> {
    Mono<Users> findByUsername(String username);
    Mono<Users> findByEmail(String email);
    Mono<Users> findByUsernameOrEmail(String username, String password);
}
