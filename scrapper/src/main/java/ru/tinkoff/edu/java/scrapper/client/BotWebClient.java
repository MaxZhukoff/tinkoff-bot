package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;

@RequiredArgsConstructor
public class BotWebClient {
    private final String baseUrl;

    public void sendUpdate(LinkUpdateRequest body) {
        WebClient.create(baseUrl)
            .post()
            .uri("/updates")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
