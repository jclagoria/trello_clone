package com.trello.authentication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.processing.Generated;
import java.time.Instant;

@Table("refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RefreshTokens {

    @Id
    private Long id;
    private String token;

    @Column("user_id")
    private Long userId;

    @Column("issued_at")
    private Instant issuedAt;

    @Column("expires_at")
    private Instant expiresAt;

}
