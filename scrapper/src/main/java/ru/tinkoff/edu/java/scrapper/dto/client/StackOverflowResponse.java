package ru.tinkoff.edu.java.scrapper.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowResponse(
    @JsonProperty("last_activity_date")
    OffsetDateTime updatedAt,
    @JsonProperty("answer_count")
    Integer answerCount
) {
}
