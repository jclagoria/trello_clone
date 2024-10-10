package com.trello.authentication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("users")
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {

    @Id
    private Long id;
    private String username;
    private String email;

    @Column("created_at")
    private Instant createdAt = Instant.now();
}