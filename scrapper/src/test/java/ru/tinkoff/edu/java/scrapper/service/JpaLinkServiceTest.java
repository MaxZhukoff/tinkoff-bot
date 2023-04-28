package ru.tinkoff.edu.java.scrapper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.dto.UpdateLinkDto;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.mapper.LinkMapper;
import ru.tinkoff.edu.java.scrapper.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkService linkService;
    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaTgChatRepository tgChatRepository;
    @Autowired
    private LinkMapper linkMapper;

    private Chat chat;

    @BeforeEach
    void setup() {
        chat = new Chat();
        chat.setId(1L);
        tgChatRepository.save(chat);
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldAddLink() {
        String link = "https://github.com/MaxZhukoff/tinkoff-bot";
        AddLinkRequest request = new AddLinkRequest(link);

        LinkResponse linkResponse = linkService.add(request, chat.getId());

        assertNotNull(linkResponse.id());
        assertEquals(linkResponse.url(), URI.create(link));
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldAddLinkToChat_whenExistingLink() {
        String link = "https://github.com/MaxZhukoff/tinkoff-bot";
        Link linkEntity = Link.builder()
                .url(link)
                .updatedAt(OffsetDateTime.now())
                .lastCheckAt(OffsetDateTime.now())
                .build();
        linkEntity.addChat(chat);
        linkRepository.save(linkEntity);
        AddLinkRequest request = new AddLinkRequest(link);

        LinkResponse linkResponse = linkService.add(request, chat.getId());

        assertNotNull(linkResponse.id());
        assertEquals(linkResponse.url(), URI.create(link));
        assertTrue(linkRepository.findByUrl(link).isPresent());
        assertTrue(linkRepository.findByUrlAndChatsId(link, chat.getId()).isPresent());
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldThrowException_whenUrlIsIncorrect() {
        String link = "not_a_url";
        AddLinkRequest request = new AddLinkRequest(link);

        assertThrows(ResponseStatusException.class, () -> linkService.add(request, chat.getId()));
        assertFalse(linkRepository.findByUrl(link).isPresent());
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldRemoveLinkChat() {
        String link = "https://github.com/MaxZhukoff/tinkoff-bot";
        Link linkEntity = Link.builder()
                .url(link)
                .updatedAt(OffsetDateTime.now())
                .lastCheckAt(OffsetDateTime.now())
                .build();
        linkEntity.addChat(chat);
        linkRepository.save(linkEntity);
        RemoveLinkRequest request = new RemoveLinkRequest(link);

        LinkResponse linkResponse = linkService.remove(request, chat.getId());

        assertNotNull(linkResponse.id());
        assertEquals(linkResponse.url(), URI.create(link));
        assertFalse(linkRepository.findByUrlAndChatsId(link, chat.getId()).isPresent());
        assertFalse(linkRepository.findByUrl(link).isPresent());
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldThrowException_whenLinkChatNotFound() {
        String link = "https://github.com/MaxZhukoff/tinkoff-bot";
        RemoveLinkRequest request = new RemoveLinkRequest(link);

        assertThrows(ResponseStatusException.class, () -> linkService.remove(request, chat.getId()));
    }

    @Transactional
    @Rollback
    @Test
    void update_shouldUpdateLink() {
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCommitAt = OffsetDateTime.now();
        Integer issuesCount = 123;
        Integer answerCount = 456;
        LinkDto linkDto = new LinkDto(
                "https://github.com/MaxZhukoff/tinkoff-bot",
                updatedAt,
                null,
                null,
                null
        );
        Link linkEntity = linkMapper.toLink(linkDto);
        linkEntity.setUpdatedAt(updatedAt.minusHours(1));
        linkEntity.setLastCommitAt(lastCommitAt.minusDays(1));
        linkEntity.setIssuesCount(0);
        linkEntity.setAnswerCount(0);
        linkRepository.save(linkEntity);
        UpdateLinkDto updateLinkDto = new UpdateLinkDto(
                linkEntity.getId(),
                updatedAt,
                lastCommitAt,
                issuesCount,
                answerCount
        );

        Link updatedLinkEntity = linkService.update(updateLinkDto);

        assertNotNull(updatedLinkEntity);
        assertEquals(updatedAt, updatedLinkEntity.getUpdatedAt());
        assertEquals(lastCommitAt, updatedLinkEntity.getLastCommitAt());
        assertEquals(issuesCount, updatedLinkEntity.getIssuesCount());
        assertEquals(answerCount, updatedLinkEntity.getAnswerCount());
    }

    @Transactional
    @Rollback
    @Test
    public void getChatsId_shouldReturnChatsId() {
        Link link = Link.builder()
                .url("https://github.com/MaxZhukoff/tinkoff-bot")
                .updatedAt(OffsetDateTime.now())
                .lastCheckAt(OffsetDateTime.now())
                .build();
        link.addChat(chat);
        linkRepository.save(link);

        List<Long> chatsId = linkService.getChatsId(link.getId());

        assertTrue(chatsId.contains(chat.getId()));
    }
}
