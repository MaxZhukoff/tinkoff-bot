package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.mapper.LinkMapper;
import ru.tinkoff.edu.java.scrapper.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.HttpLinkParser;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(
            HttpLinkParser httpLinkParser,
            LinkMapper linkMapper,
            JpaLinkRepository linkRepository,
            JpaTgChatRepository tgChatRepository
    ) {
        return new JpaLinkService(httpLinkParser, linkMapper, linkRepository, tgChatRepository);
    }

    @Bean
    public TgChatService tgChatService(JpaTgChatRepository jpaTgChatRepository) {
        return new JpaTgChatService(jpaTgChatRepository);
    }
}
