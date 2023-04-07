package ru.tinkoff.edu.java.bot.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.dto.RemoveLinkRequest;

@RequiredArgsConstructor
public class ScrapperClient {
    private final String baseUrl;

    public void fetchRegisterChat(Long id) {
        WebClient.create(baseUrl)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/tg-chat/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void fetchDeleteChat(Long id) {
        WebClient.create(baseUrl)
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/tg-chat/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public ListLinksResponse fetchGetAllTrackedLinks(Long id) {
        return WebClient.create(baseUrl)
                .get()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(id))
                .retrieve()
                .bodyToMono(ListLinksResponse.class)
                .block();
    }

    public LinkResponse fetchAddLinkTracking(Long id, AddLinkRequest addLinkRequest) {
        return WebClient.create(baseUrl)
                .post()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(id))
                .bodyValue(addLinkRequest)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    public LinkResponse fetchRemoveLinkTracking(Long id, RemoveLinkRequest removeLinkRequest) {
        return WebClient.create(baseUrl)
                .method(HttpMethod.DELETE)
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(id))
                .bodyValue(removeLinkRequest)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }
}
