package ar.com.api.trello.authentication.configuration;

import ar.com.api.trello.authentication.configuration.security.CorsProperties;
import ar.com.api.trello.authentication.security.JwtReactiveAuthenticationManager;
import ar.com.api.trello.authentication.security.JwtTokenFilter;
import ar.com.api.trello.authentication.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CorsProperties corsProperties;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CorsProperties corsProperties, JwtTokenProvider jwtTokenProvider) {
        this.corsProperties = corsProperties;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // Instantiate the custom ReactiveAuthenticationManager
        ReactiveAuthenticationManager authenticationManager = new JwtReactiveAuthenticationManager(jwtTokenProvider);

        // Create the JwtTokenFilter with the JwtTokenProvider and the custom authentication manager
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, authenticationManager);


        http.csrf(csrfSpec -> csrfSpec
                        .requireCsrfProtectionMatcher(ServerWebExchangeMatchers.pathMatchers("/secured/**"))
                )
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/service/status", "/actuator/health",
                                "/actuator/info").permitAll()
                        .pathMatchers("/api/service/account/create").permitAll()
                        .anyExchange().authenticated() // Secure all other routes
                )
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .addFilterAt(jwtTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://127.0.0.1:5175"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply to all paths
        return source;
    }
}
