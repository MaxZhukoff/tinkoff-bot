package ru.tinkoff.edu.java.bot.configuration;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@Configuration
public class RabbitMQConfiguration {
    private final String exchangeName;
    private final String queueName;

    private static final String DEAD_LETTER_SUFFIX = ".dlq";

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
            .withArgument("x-dead-letter-exchange", queueName + DEAD_LETTER_SUFFIX)
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
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange(queueName + DEAD_LETTER_SUFFIX, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(queueName + DEAD_LETTER_SUFFIX).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder
            .bind(deadLetterQueue)
            .to(deadLetterExchange);
    }

    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.dto.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }
}
