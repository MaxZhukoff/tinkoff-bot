package ru.tinkoff.edu.java.scrapper.service.sender;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.service.rabbitmq.ScrapperQueueProducer;

@RequiredArgsConstructor
public class QueueUpdateSender implements UpdateSender {
    private final ScrapperQueueProducer queueProducer;

    @Override
    public void sendUpdate(LinkUpdateRequest linkUpdateRequest) {
        queueProducer.send(linkUpdateRequest);
    }
}
