package eu.bigbank.dragons_of_mugloar.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "game.settings")
public class GameSettingsConfig {

    private int goal;
    private int turnSlack;

}
