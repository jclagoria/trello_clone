package ar.com.api.trello.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(csrfSpec -> csrfSpec
                        .requireCsrfProtectionMatcher(ServerWebExchangeMatchers.pathMatchers("/secured/**"))
                )
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/service/status", "/actuator/health",
                                "/actuator/info").permitAll()
                        .pathMatchers("/api/service/account/create").permitAll()
                        .anyExchange().authenticated() // Secure all other routes
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        return http.build();
    }
}
