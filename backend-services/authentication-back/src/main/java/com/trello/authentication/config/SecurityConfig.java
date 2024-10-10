package com.trello.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http.authorizeExchange(exchange ->
                        exchange.pathMatchers("/api/accounts/**")
                                .permitAll().anyExchange().authenticated()
                )
                .oauth2Login(oAuth2Login -> {})
                .oauth2ResourceServer(oAuth2ResourceServer -> {})
                .csrf(csrfSpec -> {})
                .build();
    }

}
