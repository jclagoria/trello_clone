package ar.com.api.trello.authentication.services.impl;

import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.model.UsersLogin;
import ar.com.api.trello.authentication.repository.UserLoginRepository;
import ar.com.api.trello.authentication.repository.UsersRepository;
import ar.com.api.trello.authentication.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final UsersRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    public AccountServiceImpl(UsersRepository userRepository, UserLoginRepository userLoginRepository) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public Mono<Object> createAccount(String username, String password, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .flatMap(existingUser -> Mono.error(new RuntimeException("Username or email already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    // Create the new user if username and email are unique
                    Users newUser = new Users();
                    newUser.setUsername(username);
                    newUser.setEmail(email);

                    return userRepository.save(newUser)
                            .flatMap(savedUser -> {
                                // Create UsersLogin record for authentication
                                UsersLogin userLogin = new UsersLogin();
                                userLogin.setUserId(savedUser.getId());
                                userLogin.setPasswordHash(password); // Assuming password is hashed
                                userLogin.setAuthProvider("email");

                                return userLoginRepository.save(userLogin)
                                        .thenReturn(savedUser); // Return Mono<Users> after saving UsersLogin
                            })
                            .onErrorResume(error -> {
                                // Handle error and propagate the error message
                                logger.error("Error creating account for username: {}, email: {}. Error: {}", username, email, error.getMessage());
                                return Mono.error(new RuntimeException("Error while creating account", error));
                            });
                }));
    }
}
