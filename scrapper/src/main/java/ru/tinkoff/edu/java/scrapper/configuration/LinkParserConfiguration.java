package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.parser.LinkParser;
import ru.tinkoff.edu.java.parser.handler.GitHubLinkHandler;
import ru.tinkoff.edu.java.parser.handler.StackOverflowLinkHandler;

import java.util.List;

@Configuration
public class LinkParserConfiguration {
    @Bean
    public LinkParser linkParser() {
        return new LinkParser(List.of(new StackOverflowLinkHandler(), new GitHubLinkHandler()));
    }
}
