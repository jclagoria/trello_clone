package ar.com.api.trello.authentication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("user_login")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsersLogin {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("password_hash")
    private String passwordHash;

    @Column("auth_provider")
    private String authProvider;

    @Column("auth_provider_id")
    private String authProviderId;

    @Column("created_at")
    private Instant createdAt = Instant.now();

    @Column("updated_at")
    private Instant updatedAt;

}
