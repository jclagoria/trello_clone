package ar.com.api.trello.authentication.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trello")
@Getter
@Setter
public class ApiServiceConfig {

    private String baseURL;
    private String healthAPI;
}
