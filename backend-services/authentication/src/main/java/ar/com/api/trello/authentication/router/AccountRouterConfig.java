package ar.com.api.trello.authentication.router;

import ar.com.api.trello.authentication.handler.AccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class AccountRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> accountRouter(AccountHandler accountHandler) {
        return route()
                .POST("/api/service/account/create", accountHandler::createAccount)
                .build();
    }

}
