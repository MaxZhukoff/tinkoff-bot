package ru.tinkoff.edu.java.scrapper.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

@AllArgsConstructor
@NoArgsConstructor
public class GitHubWebClient {
    private String baseUrl = "https://api.github.com";

    public GitHubResponse fetchRepository(String username, String repository) {
        return WebClient.builder().build()
                .get()
                .uri(baseUrl + "/repos/" + username + "/" + repository)
                .retrieve()
                .bodyToMono(GitHubResponse.class)
                .block();
    }
}
