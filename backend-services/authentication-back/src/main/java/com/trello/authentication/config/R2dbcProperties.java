package com.trello.authentication.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.r2dbc")
@Getter
@Setter
@NoArgsConstructor
public class R2dbcProperties {

    private String url;
    private String username;
    private String password;

}
