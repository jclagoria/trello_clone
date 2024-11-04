package ar.com.api.trello.authentication.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider tokenProvider;

    public JwtReactiveAuthenticationManager(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String token = authentication.getCredentials().toString();

        if(tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsername(token);
            Collection<? extends GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
            return Mono.just(new UsernamePasswordAuthenticationToken(username, token, authorities));
        }

        return Mono.empty();
    }
}
