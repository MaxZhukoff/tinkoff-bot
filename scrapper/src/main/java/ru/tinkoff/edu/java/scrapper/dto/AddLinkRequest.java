package ru.tinkoff.edu.java.scrapper.dto;

import jakarta.validation.constraints.NotBlank;

import java.net.URI;

public record AddLinkRequest(
        @NotBlank
        URI link
) {
}
