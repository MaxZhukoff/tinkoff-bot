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
    private static final String TG_CHAT_PATH = "/tg-chat/{id}";
    private static final String LINKS_URI = "/links";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    public void fetchRegisterChat(Long id) {
        WebClient.create(baseUrl)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(TG_CHAT_PATH)
                        .build(id))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void fetchDeleteChat(Long id) {
        WebClient.create(baseUrl)
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(TG_CHAT_PATH)
                        .build(id))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public ListLinksResponse fetchGetAllTrackedLinks(Long id) {
        return WebClient.create(baseUrl)
                .get()
                .uri(LINKS_URI)
                .header(TG_CHAT_ID_HEADER, String.valueOf(id))
                .retrieve()
                .bodyToMono(ListLinksResponse.class)
                .block();
    }

    public LinkResponse fetchAddLinkTracking(Long id, AddLinkRequest addLinkRequest) {
        return WebClient.create(baseUrl)
                .post()
                .uri(LINKS_URI)
                .header(TG_CHAT_ID_HEADER, String.valueOf(id))
                .bodyValue(addLinkRequest)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    public LinkResponse fetchRemoveLinkTracking(Long id, RemoveLinkRequest removeLinkRequest) {
        return WebClient.create(baseUrl)
                .method(HttpMethod.DELETE)
                .uri(LINKS_URI)
                .header(TG_CHAT_ID_HEADER, String.valueOf(id))
                .bodyValue(removeLinkRequest)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }
}
