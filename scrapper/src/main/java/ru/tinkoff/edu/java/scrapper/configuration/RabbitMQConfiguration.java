package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQConfiguration {
    private final String exchangeName;
    private final String queueName;

    public RabbitMQConfiguration(ApplicationConfig config) {
        this.exchangeName = config.rabbitMQ().exchangeName();
        this.queueName = config.rabbitMQ().queueName();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", queueName + ".dlq")
                .build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .withQueueName();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
