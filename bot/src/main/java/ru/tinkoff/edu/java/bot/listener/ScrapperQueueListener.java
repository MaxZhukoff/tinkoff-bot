package ru.tinkoff.edu.java.bot.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.UpdateService;

@RequiredArgsConstructor
@Service
@RabbitListener(queues = "${app.rabbitmq.queue-name}")
public class ScrapperQueueListener {
    private final UpdateService updateService;

    @RabbitHandler
    public void receiver(LinkUpdateRequest linkUpdateRequest) {
        updateService.handleUpdate(linkUpdateRequest);
    }
}
