package com.trello.authentication.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class R2DBConfig {

    @Bean
    @Profile("dev-memory")
    public ConnectionFactory inMemoryConnectionFactory(R2dbcProperties memoryProps) {
        return ConnectionFactoryBuilder.withUrl(memoryProps.getUrl())
                .username(memoryProps.getUsername())
                .password(memoryProps.getPassword())
                .build();
    }

    @Bean
    @Profile("dev-database")
    public ConnectionFactory postgresConnectionFactory(R2dbcProperties dbProps) {
        return ConnectionFactoryBuilder.withUrl(dbProps.getUrl())
                .username(dbProps.getUsername())
                .password(dbProps.getPassword()).build();
    }

}
