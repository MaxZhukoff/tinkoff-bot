package ru.tinkoff.edu.java.scrapper.configuration.sender;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.BotWebClient;
import ru.tinkoff.edu.java.scrapper.service.sender.HttpUpdateSender;
import ru.tinkoff.edu.java.scrapper.service.sender.UpdateSender;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpUpdateSenderConfiguration {
    @Bean
    public UpdateSender updateSender(BotWebClient botWebClient) {
        return new HttpUpdateSender(botWebClient);
    }
}
