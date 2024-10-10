package com.trello.authentication.config.oauths;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class OAuth2LoginSuccessHandler {

    private final String jwtSecret = "jwtSecret";

    public Mono<Void> onAuthenticationSuccess(ServerWebExchange exchange, Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        OidcUserInfo userInfo = oidcUser.getUserInfo();

        String email = userInfo.getEmail();
        String name = userInfo.getFullName();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(10, ChronoUnit.DAYS);

        String token = Jwts.builder()
                .subject(email)
                .claim("name", name)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), Jwts.SIG.HS256)
                .compact();

        exchange.getResponse()
                .addCookie(ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(true)
                        .path("/").maxAge(900)
                        .build());

        return Mono.empty();
    }

}
