package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotWebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class LinkUpdater {
    private final Long checkDelayMinutes;
    private final LinkService linkService;
    private final HttpLinkParser httpLinkParser;
    private final BotWebClient botWebClient;

    public void update() {
        for (Link l : linkService.getAllLinksNotUpdatedAt(checkDelayMinutes)) {
            LinkDto linkDto = httpLinkParser.parse(l.getUrl());

            if (linkDto.updatedAt().isAfter(l.getUpdatedAt())) {
                Link link = linkService.update(new UpdateLinkDto(l.getId(), linkDto.updatedAt()));
                sendUpdateToBot(new LinkUpdateRequest(
                        link.getId(),
                        URI.create(link.getUrl()),
                        "Отслеживаемая ссылка обновилась",
                        linkService.getChatsId(link.getId())
                ));
            } else {
                linkService.updateLastCheck(l.getId());
            }
        }
    }

    private void sendUpdateToBot(LinkUpdateRequest linkUpdateRequest) {
        botWebClient.sendUpdate(linkUpdateRequest);
    }
}
