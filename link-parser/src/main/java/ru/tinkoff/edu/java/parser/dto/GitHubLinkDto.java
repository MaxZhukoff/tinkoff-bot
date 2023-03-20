package ru.tinkoff.edu.java.parser.dto;

public record GitHubLinkDto(
        String username,
        String repository
) implements LinkDto {
}
