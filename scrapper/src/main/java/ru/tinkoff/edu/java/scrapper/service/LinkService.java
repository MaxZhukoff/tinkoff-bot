package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.util.List;

public interface LinkService {
    LinkResponse add(AddLinkRequest addLinkRequest, Long tgChatId);
    LinkResponse remove(RemoveLinkRequest removeLinkRequest, Long tgChatId);
    Link update(UpdateLinkDto link);
    void updateLastCheck(Long linkId);
    List<Long> getChatsId(Long linkId);
    ListLinksResponse getAllTrackedLinks(Long tgChatId);
    List<Link> getAllLinksNotUpdatedAt(Long delayUpdatedMinutes);
}
