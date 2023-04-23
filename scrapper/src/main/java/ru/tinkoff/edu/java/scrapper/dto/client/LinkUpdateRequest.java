package ru.tinkoff.edu.java.scrapper.dto.client;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
        @NotNull
        @Min(0)
        Long id,

        @NotNull
        URI url,

        @NotBlank
        String description,

        @NotEmpty
        List<@NotNull @Min(0) Long> tgChatIds
) {
}
