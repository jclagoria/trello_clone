package com.trello.authentication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("blacklisted_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BlacklistedTokens {

    @Id
    private Long id;

    private String token;

    @Column("blacklisted_at")
    private Instant blacklistedAt;

}
