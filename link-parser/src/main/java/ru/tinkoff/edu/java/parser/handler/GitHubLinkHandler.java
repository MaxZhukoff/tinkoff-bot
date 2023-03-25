package ru.tinkoff.edu.java.parser.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.dto.GitHubLinkDto;
import ru.tinkoff.edu.java.parser.dto.LinkDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class GitHubLinkHandler implements LinkHandler {
    private final String gitHubRegex = "https://github.com/([\\w-]+)/([\\w-]+)";
    private final Pattern gitHubPattern = Pattern.compile(gitHubRegex, Pattern.CASE_INSENSITIVE);
    @Override
    public @Nullable LinkDto parse(@NotNull String url) {
        Matcher matcher = gitHubPattern.matcher(url);
        if (matcher.find()) {
            return new GitHubLinkDto(matcher.group(1), matcher.group(2));
        }

        return null;
    }
}
