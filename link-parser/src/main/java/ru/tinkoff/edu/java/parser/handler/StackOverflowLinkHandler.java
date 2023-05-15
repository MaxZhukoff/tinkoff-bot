package ru.tinkoff.edu.java.parser.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.dto.LinkDto;
import ru.tinkoff.edu.java.parser.dto.StackOverflowLinkDto;

@Component
public final class StackOverflowLinkHandler implements LinkHandler {
    private final String stackOverflowRegex = "https://stackoverflow.com/questions/(\\d+)/";
    private final Pattern stackOverflowPattern = Pattern.compile(stackOverflowRegex, Pattern.CASE_INSENSITIVE);

    @Override
    public @Nullable LinkDto parse(@NotNull String url) {
        Matcher matcher = stackOverflowPattern.matcher(url);
        if (matcher.find()) {
            return new StackOverflowLinkDto(Integer.parseInt(matcher.group(1)));
        }

        return null;
    }
}
