package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Bot bot,
        @NotNull Scrapper scrapper,
        @NotNull RabbitMQ rabbitMQ
) {
    record Bot(String token, String name) {}

    record Scrapper(String url) {}

    record RabbitMQ(String exchangeName, String queueName) {}
}
