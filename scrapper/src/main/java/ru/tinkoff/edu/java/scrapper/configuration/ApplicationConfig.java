package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull String test,
    @NotNull Scheduler scheduler,
    @NotNull Bot bot,
    @NotNull RabbitMQ rabbitMQ,
    @NotNull AccessType databaseAccessType,
    @NotNull Boolean useQueue
) {
    record Scheduler(Duration interval, Long checkDelayMinutes) {
    }

    record Bot(String url) {
    }

    record RabbitMQ(String exchangeName, String queueName) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
