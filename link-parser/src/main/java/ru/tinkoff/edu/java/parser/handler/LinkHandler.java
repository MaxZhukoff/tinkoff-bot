package ru.tinkoff.edu.java.parser.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.tinkoff.edu.java.parser.dto.LinkDto;

public sealed interface LinkHandler permits GitHubLinkHandler, StackOverflowLinkHandler {
    @Nullable LinkDto parse(@NotNull String url);
}
