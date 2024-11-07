package ar.com.api.trello.authentication.services.impl;

import ar.com.api.trello.authentication.model.Users;
import ar.com.api.trello.authentication.model.UsersLogin;
import ar.com.api.trello.authentication.repository.UserLoginRepository;
import ar.com.api.trello.authentication.repository.UsersRepository;
import ar.com.api.trello.authentication.security.JwtTokenProvider;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private UsersRepository userRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AccountServiceImpl accountService;

    private static Faker dataFaker;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataFaker = new Faker();
    }

    @Test
    @DisplayName("Should return an object with the data for the new user")
    void createAccountSuccess() {
        String username = dataFaker.internet().username();
        String password = dataFaker.internet().password(true);
        String email = dataFaker.internet().emailAddress();

        Users newUser = new Users();
        newUser.setId(1l);
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
    @DisplayName("Should return an Error when find a registry with the same email")
    void createAccountUsernameOrEmailExits() {
        String username = dataFaker.internet().username();
        String email = dataFaker.internet().emailAddress();

        Users existingUser = new Users();
        existingUser.setUsername(username);
        existingUser.setEmail(email);

        when(userRepository.findByUsernameOrEmail(username, email))
                .thenReturn(Mono.just(existingUser));

        StepVerifier.create(accountService
                        .createAccount(username, dataFaker.internet().password(), email))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Username or email already exists"))
                .verify();

        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(0)).save(any(Users.class));
    }

    @Test
    @DisplayName("Should return an Error when a Save a new User")
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

    @Test
    @DisplayName("Should return an Map with Token and Users Object")
    void testLogin_Success() {
        // Arrange
        String email = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password(6, 8, true, false, true);  // Raw password
        String username = dataFaker.internet().username();
        String encodedPassword = passwordEncoder.encode(password);  // Encode the password for storage
        long idUser = dataFaker.number().randomNumber();
        long idUserLogin = dataFaker.number().randomNumber();
        String token = dataFaker.lorem().characters(45);

        Users user = new Users();
        user.setId(idUser);
        user.setEmail(email);
        user.setUsername(username);

        UsersLogin usersLogin = new UsersLogin();
        usersLogin.setId(idUserLogin);
        usersLogin.setUserId(idUser);
        usersLogin.setPasswordHash(encodedPassword);  // Store the encoded password

        // Mock repository and token provider responses
        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(jwtTokenProvider.createToken(username, String.valueOf(idUser))).thenReturn(token);
        when(userLoginRepository.findByUserId(idUser)).thenReturn(Mono.just(usersLogin));

        // Act
        Mono<Object> resultExpected = accountService.login(email, encodedPassword);

        // Assert
        StepVerifier.create(resultExpected)
                .expectNextMatches(response -> {
                    Map<String, Object> responseMap = (Map<String, Object>) response;
                    return responseMap.get("token").equals(token) &&
                            ((Users) responseMap.get("user")).getUsername().equals(username) &&
                            ((Users) responseMap.get("user")).getEmail().equals(email);
                })
                .verifyComplete();

        verify(jwtTokenProvider, times(1)).createToken(username, String.valueOf(idUser));
    }

    @Test
    @DisplayName("Should return an error for Invalid Password")
    void testLogin_InvalidPassword() {
        String email = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password(6, 8, true, false, true);
        String wrongPassword = dataFaker.internet().password(6, 8, true, false, true);
        String username = dataFaker.internet().username();
        String encodedCorrectPassword = passwordEncoder.encode(password);
        String encodeWrongPassword = passwordEncoder.encode(wrongPassword);
        long idUser = dataFaker.number().randomNumber();
        long idUserLogin = dataFaker.number().randomNumber();

        Users user = new Users();
        user.setId(idUser);
        user.setEmail(email);
        user.setUsername(username);

        UsersLogin usersLogin = new UsersLogin();
        usersLogin.setId(idUserLogin);
        usersLogin.setUserId(idUser);
        usersLogin.setPasswordHash(encodedCorrectPassword);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(userLoginRepository.findByUserId(idUser)).thenReturn(Mono.just(usersLogin));

        Mono<Object> resultExpected = accountService.login(email, encodeWrongPassword);

        StepVerifier.create(resultExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Invalid credentials"))
                .verify();
    }

    @Test
    @DisplayName("Should return an error for User not Found")
    void testLogin_UserNotFound() {
        String emailNonExistent = dataFaker.internet().emailAddress();
        String password = dataFaker.internet().password(6, 8, true, false, true);

        when(userRepository.findByEmail(emailNonExistent)).thenReturn(Mono.empty());

        Mono<Object> resultExpected = accountService.login(emailNonExistent, password);

        StepVerifier.create(resultExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("User not found"))
                .verify();

        verify(userLoginRepository, never()).findByUserId(anyLong());
        verify(jwtTokenProvider, never()).createToken(anyString(), anyString());
    }
}