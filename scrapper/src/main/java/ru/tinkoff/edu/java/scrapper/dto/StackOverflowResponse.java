package ru.tinkoff.edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackOverflowResponse(
        @JsonProperty("question_id")
        @JsonFormat( with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        String questionId,
        String title,
        @JsonProperty("view_count")
        String viewCount,
        @JsonProperty("last_activity_date")
        OffsetDateTime lastActivityDate
) {
}
