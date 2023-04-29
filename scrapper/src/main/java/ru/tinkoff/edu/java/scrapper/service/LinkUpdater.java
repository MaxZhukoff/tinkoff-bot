package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.dto.client.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.service.sender.UpdateSender;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class LinkUpdater {
    private final Long checkDelayMinutes;
    private final LinkService linkService;
    private final HttpLinkParser httpLinkParser;
    private final UpdateSender updateSender;

    public void update() {
        for (Link l : linkService.getAllLinksNotUpdatedAt(checkDelayMinutes)) {
            LinkDto linkDto = httpLinkParser.parse(l.getUrl());

            if (linkDto.updatedAt().isAfter(l.getUpdatedAt())) {

                String description = getUpdateDescription(l, linkDto);

                Link link = linkService.update(new UpdateLinkDto(
                        l.getId(),
                        linkDto.updatedAt(),
                        linkDto.lastCommitAt(),
                        linkDto.issuesCount(),
                        linkDto.answerCount()
                ));
                updateSender.sendUpdate(new LinkUpdateRequest(
                        link.getId(),
                        URI.create(link.getUrl()),
                        description,
                        linkService.getChatsId(link.getId())
                ));
            } else {
                linkService.updateLastCheck(l.getId());
            }
        }
    }

    private String getUpdateDescription(Link oldLink, LinkDto newLinkDto) {
        StringBuilder sb = new StringBuilder();
        if (newLinkDto.lastCommitAt() != null && newLinkDto.lastCommitAt().isAfter(oldLink.getLastCommitAt()))
            sb.append("Добавлен новый коммит\n");
        if (newLinkDto.issuesCount() != null && !newLinkDto.issuesCount().equals(oldLink.getIssuesCount()))
            sb.append("Количество тикетов обновлено\n");
        if (newLinkDto.answerCount() != null && !newLinkDto.answerCount().equals(oldLink.getAnswerCount()))
            sb.append("Количество ответов обновлено\n");
        return sb.length() > 0 ? sb.toString() : "Отслеживаемая ссылка обновилась";
    }
}
