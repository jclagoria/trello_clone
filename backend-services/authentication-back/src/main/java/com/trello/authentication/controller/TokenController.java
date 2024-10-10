package com.trello.authentication.controller;

import com.trello.authentication.service.impl.RefreshTokenServiceImpl;
import com.trello.authentication.utils.JwtUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenController {

    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtUtils jwtUtils;

    public TokenController(RefreshTokenServiceImpl refreshTokenService, JwtUtils jwtUtils) {
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/refresh")
    public Mono<Void> refreshToken(@CookieValue("refreshToken") String refreshToken, ServerWebExchange exchange) {
        
    }

}
