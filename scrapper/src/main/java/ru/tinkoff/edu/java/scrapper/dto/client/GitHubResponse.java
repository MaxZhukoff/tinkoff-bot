package ru.tinkoff.edu.java.scrapper.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GitHubResponse(
        @JsonProperty("updated_at")
        OffsetDateTime updatedAt,
        @JsonProperty("pushed_at")
        OffsetDateTime lastCommitAt,
        @JsonProperty("open_issues_count")
        Integer issuesCount
) {
}
