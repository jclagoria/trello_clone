package com.trello.authentication.service.impl;

import com.trello.authentication.model.User;
import com.trello.authentication.model.UserLogin;
import com.trello.authentication.repository.UserLoginRepository;
import com.trello.authentication.repository.UserRepository;
import com.trello.authentication.service.AccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    public AccountServiceImpl(UserRepository userRepository, UserLoginRepository userLoginRepository) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
    }


    @Override
    public Mono<User> createAccount(String username, String email, String password) {
        User user = User.builder().username(username).email(email).build();

        return userRepository.save(user).flatMap(saveUser -> {
            UserLogin userLogin = UserLogin.builder()
                    .userId(saveUser.getId())
                    .passwordHash(password).authProvider("email")
                    .build();
            return userLoginRepository.save(userLogin).then(Mono.just(saveUser));
        });
    }
}
