package ar.com.api.trello.authentication.services.impl;

import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.model.UsersLogin;
import ar.com.api.trello.authentication.repository.UserLoginRepository;
import ar.com.api.trello.authentication.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private UsersRepository userRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccountSuccess() {
        String username = "test_user";
        String password = "test_password";
        String email = "test_email@gmail.com";

        Users newUser = new Users();
        newUser.setId(1L);
        newUser.setUsername(username);
        newUser.setEmail(email);

        UsersLogin usersLogin = new UsersLogin();
        usersLogin.setUserId(newUser.getId());
        usersLogin.setPasswordHash(password);

        when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(Mono.empty());
        when(userRepository.save(any(Users.class))).thenReturn(Mono.just(newUser));
        when(userLoginRepository.save(any(UsersLogin.class))).thenReturn(Mono.just(usersLogin));

        StepVerifier.create(accountService.createAccount(username, password, email))
                .expectNext(newUser)
                .verifyComplete();

        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(1)).save(any(Users.class));
        verify(userLoginRepository, times(1)).save(any(UsersLogin.class));
    }

    @Test
    void createAccountUsernameOrEmailExits() {
        String username = "existinguser";
        String email = "existinguser@example.com";

        Users existingUser = new Users();
        existingUser.setUsername(username);
        existingUser.setEmail(email);

        when(userRepository.findByUsernameOrEmail(username, email))
                .thenReturn(Mono.just(existingUser));

        StepVerifier.create(accountService.createAccount(username, "password", email))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Username or email already exists"))
                .verify();

        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(0)).save(any(Users.class));
    }

    @Test
    void createAccountErrorDuringSave() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "hashedPassword";

        Users newUser = new Users();
        newUser.setId(1L);
        newUser.setUsername(username);
        newUser.setEmail(email);

        when(userRepository.findByUsernameOrEmail(username, email))
                .thenReturn(Mono.empty());
        when(userRepository.save(any(Users.class))).thenReturn(Mono.just(newUser));
        when(userLoginRepository.save(any(UsersLogin.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(accountService.createAccount(username, password, email))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Error while creating account"))  // Expect the new message
                .verify();

        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(1)).save(any(Users.class));
        verify(userLoginRepository, times(1)).save(any(UsersLogin.class));
    }

}