package ru.tinkoff.edu.java.scrapper.dto.client;

import java.util.List;

public record StackOverflowItemsResponse(
        List<StackOverflowResponse> items
) {
}
