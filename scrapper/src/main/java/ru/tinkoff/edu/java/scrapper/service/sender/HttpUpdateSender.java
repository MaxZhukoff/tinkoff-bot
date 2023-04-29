package ru.tinkoff.edu.java.scrapper.service.sender;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.client.BotWebClient;
import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;

@RequiredArgsConstructor
public class HttpUpdateSender implements UpdateSender {
    private final BotWebClient botWebClient;
    @Override
    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        botWebClient.sendUpdate(linkUpdateRequest);
    }
}
