package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.HttpLinkParser;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqTgChatService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService linkService(HttpLinkParser httpLinkParser, DSLContext dslContext) {
        return new JooqLinkService(httpLinkParser, dslContext);
    }

    @Bean
    public TgChatService tgChatService(DSLContext dslContext) {
        return new JooqTgChatService(dslContext);
    }
}
