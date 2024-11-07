package ar.com.api.trello.authentication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey =  Jwts.SIG.HS256.key().build();
    }

    public String createToken(String username, String userId) {
        ClaimsBuilder claims = Jwts.claims().subject(username);
        claims.add("userId", userId);

        Instant now = Instant.now();
        Instant validity = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .claims(claims.build())
                .issuedAt(Date.from(now))
                .expiration(Date.from(validity))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey)
                    .build().parseClaimsJws(token);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody()
                .getSubject();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey).build()
                .parseEncryptedClaims(token)
                .getPayload();

        List<String> roles = claims.get("roles", List.class);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }



}
