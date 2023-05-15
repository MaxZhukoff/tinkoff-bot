package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.parser.LinkParser;
import ru.tinkoff.edu.java.parser.dto.GitHubLinkDto;
import ru.tinkoff.edu.java.parser.dto.StackOverflowLinkDto;
import ru.tinkoff.edu.java.scrapper.client.GitHubWebClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowWebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.client.StackOverflowResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class HttpLinkParser {
    private final LinkParser linkParser;
    private final GitHubWebClient gitHubWebClient;
    private final StackOverflowWebClient stackOverflowWebClient;

    public LinkDto parse(String link) {
        var linkDto = linkParser.parseLink(link);
        try {
            if (linkDto instanceof GitHubLinkDto gitHubLinkDto) {
                GitHubResponse response = gitHubWebClient.fetchRepository(
                    gitHubLinkDto.username(),
                    gitHubLinkDto.repository()
                );
                return new LinkDto(
                    link,
                    response.updatedAt(),
                    response.lastCommitAt(),
                    response.issuesCount(),
                    null
                );

            } else if (linkDto instanceof StackOverflowLinkDto stackOverflowLinkDto) {
                StackOverflowResponse response = stackOverflowWebClient.fetchQuestion(
                    String.valueOf(stackOverflowLinkDto.questionId())
                );
                return new LinkDto(
                    link,
                    response.updatedAt(),
                    null,
                    null,
                    response.answerCount()
                );

            } else {
                throw new ResponseStatusException(BAD_REQUEST, "The link is not supported");
            }

        } catch (WebClientResponseException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Incorrect link");
        }
    }
}
