package ar.com.api.trello.authentication.security;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private static Faker dataFaker;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.init();
        dataFaker = new Faker();
    }

    @Test
    void testCreateToken() {
        String username = dataFaker.internet().username();
        long userId = dataFaker.barcode().ean8();

        String token = jwtTokenProvider.createToken(username, String.valueOf(userId));
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testValidateToken_Success() {
        String username = dataFaker.internet().username();
        long userId = dataFaker.barcode().ean8();

        String token = jwtTokenProvider.createToken(username, String.valueOf(userId));
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testValidateToken_InvalidToken() {
            String invalidToken = dataFaker.lorem().characters(45);

        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void testGetUserIdFromToken() {
        String username = dataFaker.internet().username();
        long userId = dataFaker.barcode().ean8();
        String token = jwtTokenProvider.createToken(username, String.valueOf(userId));
        String extractedUserId = jwtTokenProvider.getUsername(token);

        assertEquals(username, extractedUserId);
    }

}