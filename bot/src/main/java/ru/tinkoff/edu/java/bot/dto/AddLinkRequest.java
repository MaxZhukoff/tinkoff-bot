package ru.tinkoff.edu.java.bot.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull
    URI link
) {
}
