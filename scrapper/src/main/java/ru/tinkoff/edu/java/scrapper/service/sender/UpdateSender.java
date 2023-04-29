package ru.tinkoff.edu.java.scrapper.service.sender;

import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;

public interface UpdateSender {
    void sendUpdate(LinkUpdateRequest linkUpdateRequest);
}
