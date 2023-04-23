package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.BotWebClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubWebClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowWebClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubWebClient gitHubClient() {
        return new GitHubWebClient();
    }

    @Bean
    StackOverflowWebClient stackOverflowWebClient() {
        return new StackOverflowWebClient();
    }

    @Bean
    BotWebClient botWebClient(ApplicationConfig config) {
        return new BotWebClient(config.bot().url());
    }

}
