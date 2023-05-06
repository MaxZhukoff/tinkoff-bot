package ru.tinkoff.edu.java.scrapper.configuration.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.rabbitmq.ScrapperQueueProducer;
import ru.tinkoff.edu.java.scrapper.service.sender.QueueUpdateSender;
import ru.tinkoff.edu.java.scrapper.service.sender.UpdateSender;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueUpdateSenderConfiguration {
    @Bean
    public ScrapperQueueProducer scrapperQueueProducer(RabbitTemplate rabbitTemplate, Queue queue) {
        return new ScrapperQueueProducer(rabbitTemplate, queue);
    }

    @Bean
    public UpdateSender updateSender(ScrapperQueueProducer scrapperQueueProducer) {
        return new QueueUpdateSender(scrapperQueueProducer);
    }
}
