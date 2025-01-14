package eu.bigbank.dragons_of_mugloar.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Setter
@Configuration
@ConfigurationProperties(prefix = "game.api")
public class GameApiConfig {

    private String baseUrl;

    @Bean
    public WebClient client(WebClient.Builder builder) {
        return builder.baseUrl(baseUrl).build();
    }

}
