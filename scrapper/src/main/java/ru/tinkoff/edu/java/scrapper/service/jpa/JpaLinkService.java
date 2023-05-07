package ru.tinkoff.edu.java.scrapper.service.jpa;

import jakarta.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.mapper.LinkMapper;
import ru.tinkoff.edu.java.scrapper.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.HttpLinkParser;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final HttpLinkParser httpLinkParser;
    private final LinkMapper linkMapper;
    private final JpaLinkRepository linkRepository;
    private final JpaTgChatRepository tgChatRepository;
    private static final String LINK_NOT_FOUND_DESCRIPTION = "Link not found";

    @Transactional
    @Override
    public LinkResponse add(AddLinkRequest addLinkRequest, Long tgChatId) {
        Chat chat = getChat(tgChatId);

        String link = addLinkRequest.link();
        URI uri = getURI(link);

        Optional<Link> linkOptional = linkRepository.findByUrlAndChatsId(link, tgChatId);
        if (linkOptional.isPresent()) {
            Link linkEntity = linkOptional.get();
            return linkMapper.toLinkResponse(linkEntity);
        }

        linkOptional = linkRepository.findByUrl(link);
        if (linkOptional.isPresent()) {
            Link linkEntity = linkOptional.get();
            linkEntity.addChat(chat);
            linkRepository.save(linkEntity);
            return linkMapper.toLinkResponse(linkEntity);
        }

        LinkDto linkDto = httpLinkParser.parse(link);
        Link linkEntity = linkMapper.toLink(linkDto);
        linkEntity.addChat(chat);
        linkRepository.save(linkEntity);

        return linkMapper.toLinkResponse(linkEntity);
    }

    @Transactional
    @Override
    public LinkResponse remove(RemoveLinkRequest removeLinkRequest, Long tgChatId) {
        Chat chat = getChat(tgChatId);
        String link = removeLinkRequest.link();
        URI uri = getURI(link);

        Optional<Link> linkOptional = linkRepository.findByUrlAndChatsId(link, tgChatId);
        if (linkOptional.isPresent()) {
            Link linkEntity = linkOptional.get();
            linkEntity.removeChat(chat);
            linkRepository.save(linkEntity);
            return linkMapper.toLinkResponse(linkEntity);
        } else {
            throw new ResponseStatusException(NOT_FOUND, LINK_NOT_FOUND_DESCRIPTION);
        }
    }

    @Transactional
    @Override
    public Link update(UpdateLinkDto link) {
        Link linkEntity = linkRepository.findById(link.id()).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, LINK_NOT_FOUND_DESCRIPTION)
        );

        linkEntity.setUpdatedAt(link.updatedAt());
        linkEntity.setLastCommitAt(link.lastCommitAt());
        linkEntity.setIssuesCount(link.issuesCount());
        linkEntity.setAnswerCount(link.answerCount());

        linkRepository.save(linkEntity);

        return linkEntity;
    }

    @Transactional
    @Override
    public void updateLastCheck(Long linkId) {
        Link linkEntity = linkRepository.findById(linkId).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, LINK_NOT_FOUND_DESCRIPTION)
        );

        linkEntity.setLastCheckAt(OffsetDateTime.now());
        linkRepository.save(linkEntity);
    }

    @Transactional
    @Override
    public List<Long> getChatsId(Long linkId) {
        Link linkEntity = linkRepository.findById(linkId).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, LINK_NOT_FOUND_DESCRIPTION)
        );

        return linkEntity.getChats().stream().map(Chat::getId).toList();
    }

    @Override
    public ListLinksResponse getAllTrackedLinks(Long tgChatId) {
        Chat chat = getChat(tgChatId);
        return linkMapper.toListLinksResponse(chat.getLinks());
    }

    @Override
    public List<Link> getAllLinksNotUpdatedAt(Long delayUpdatedMinutes) {
        OffsetDateTime checkTime = OffsetDateTime.now().minusMinutes(delayUpdatedMinutes);
        return linkRepository.findAllLinksNotUpdatedAt(checkTime);
    }

    private Chat getChat(Long tgChatId) {
        return tgChatRepository.findById(tgChatId).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, "Chat not found")
        );
    }

    private URI getURI(String link) {
        try {
            return new URL(link).toURI();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Incorrect link");
        }
    }
}
