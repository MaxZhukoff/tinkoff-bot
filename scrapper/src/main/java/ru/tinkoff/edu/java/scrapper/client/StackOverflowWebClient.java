package ru.tinkoff.edu.java.scrapper.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.client.StackOverflowItemsResponse;
import ru.tinkoff.edu.java.scrapper.dto.client.StackOverflowResponse;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class StackOverflowWebClient {
    private String baseUrl = "https://api.stackexchange.com";

    public StackOverflowResponse fetchQuestion(String questionId) {
        return Objects.requireNonNull(WebClient.create(baseUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/2.3/questions/{questionId}")
                        .queryParam("site", "stackoverflow")
                        .build(questionId))
                .retrieve()
                .bodyToMono(StackOverflowItemsResponse.class)
                .block()).items().get(0);
    }
}
