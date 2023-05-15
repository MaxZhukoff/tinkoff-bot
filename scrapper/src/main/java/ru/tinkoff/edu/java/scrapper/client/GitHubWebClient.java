package ru.tinkoff.edu.java.scrapper.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.client.GitHubResponse;

@AllArgsConstructor
@NoArgsConstructor
public class GitHubWebClient {
    private String baseUrl = "https://api.github.com";

    public GitHubResponse fetchRepository(String username, String repository) {
        return WebClient.create(baseUrl)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{username}/{repository}")
                .build(username, repository))
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
