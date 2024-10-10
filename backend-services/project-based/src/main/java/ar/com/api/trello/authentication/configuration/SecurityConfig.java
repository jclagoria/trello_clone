package ar.com.api.trello.authentication.configuration;

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
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/service/status", "/actuator/health", "/actuator/info").permitAll()
                        .anyExchange().authenticated() // Secure all other routes
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        return http.build();
    }

}
