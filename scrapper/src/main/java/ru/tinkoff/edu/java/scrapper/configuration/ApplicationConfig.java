package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Scheduler scheduler,
        @NotNull Bot bot,
        @NotNull AccessType databaseAccessType
) {
    record Scheduler(Duration interval, Long checkDelayMinutes) {}

    record Bot(String url) {}

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
