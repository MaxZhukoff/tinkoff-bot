package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.HttpLinkParser;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class JdbcLinkService implements LinkService {
    private final HttpLinkParser httpLinkParser;
    private final JdbcLinkRepository linkRepository;

    @Transactional
    @Override
    public LinkResponse add(AddLinkRequest addLinkRequest, Long tgChatId) {
        String link = addLinkRequest.link();
        URI uri = getURI(link);

        LinkDto linkDto = httpLinkParser.parse(link);

        long id;
        try {
            id = linkRepository.add(link, tgChatId, linkDto.updatedAt());
        } catch (EmptyResultDataAccessException e) {
            id = linkRepository.getLinkId(link);
        }

        return new LinkResponse(id, uri);
    }

    @Transactional
    @Override
    public LinkResponse remove(RemoveLinkRequest removeLinkRequest, Long tgChatId) {
        String link = removeLinkRequest.link();
        URI uri = getURI(link);
        try {
            return new LinkResponse(linkRepository.remove(link, tgChatId), uri);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(NOT_FOUND, "Link not found");
        }
    }

    @Override
    public Link update(UpdateLinkDto link) {
        return linkRepository.update(link.id(), link.updatedAt());
    }

    @Override
    public void updateLastCheck(Long linkId) {
        linkRepository.updateLastCheck(linkId);
    }

    @Override
    public List<Long> getChatsId(Long linkId) {
        return linkRepository.getChatsId(linkId);
    }

    @Override
    public ListLinksResponse getAllTrackedLinks(Long tgChatId) {
        List<LinkResponse> linkResponses = linkRepository.findAllLinkId(tgChatId);
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public List<Link> getAllLinksNotUpdatedAt(Long delayUpdatedMinutes) {
        return linkRepository.findAllLinksNotUpdatedAt(delayUpdatedMinutes);
    }

    private URI getURI(String link) {
        try {
            return new URL(link).toURI();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Incorrect link");
        }
    }
}
