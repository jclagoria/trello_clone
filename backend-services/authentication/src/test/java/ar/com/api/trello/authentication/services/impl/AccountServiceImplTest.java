package ar.com.api.trello.authentication.services.impl;

import ar.com.api.trello.authentication.repository.UserLoginRepository;
import ar.com.api.trello.authentication.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountServiceImplTest {

    @Mock
    private UsersRepository userRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testCreateAccount() {
        MockitoAnnotations.initMocks(this);


    }

}