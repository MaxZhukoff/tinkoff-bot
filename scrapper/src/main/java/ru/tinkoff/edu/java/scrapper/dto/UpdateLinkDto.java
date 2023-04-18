package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record UpdateLinkDto(
        Long id,
        OffsetDateTime updatedAt
) {
}
