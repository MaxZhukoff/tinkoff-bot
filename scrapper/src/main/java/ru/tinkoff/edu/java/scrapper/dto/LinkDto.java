package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record LinkDto(
        String url,
        OffsetDateTime updatedAt,
        OffsetDateTime lastCommitAt,
        Integer issuesCount,
        Integer answerCount
) {
}
