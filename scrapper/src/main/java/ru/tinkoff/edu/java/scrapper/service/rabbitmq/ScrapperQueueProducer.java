package ru.tinkoff.edu.java.scrapper.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;

@RequiredArgsConstructor
@Service
public class ScrapperQueueProducer {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;


    public void send(LinkUpdateRequest linkUpdateRequest) {
        rabbitTemplate.convertAndSend(queue.getName(), linkUpdateRequest);
    }
}
