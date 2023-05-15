package ru.tinkoff.edu.java.parser;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.dto.LinkDto;
import ru.tinkoff.edu.java.parser.handler.LinkHandler;

@Component
@RequiredArgsConstructor
public class LinkParser {
    private final List<LinkHandler> handlers;

    public LinkDto parseLink(String url) {
        for (LinkHandler handler : handlers) {
            LinkDto linkDto = handler.parse(url);
            if (linkDto != null) {
                return linkDto;
            }
        }
        return null;
    }
}
