package ru.tinkoff.edu.java.scrapper.dto;

public record ListLinksResponse(
        LinkResponse links,
        Integer size
) {
}
