package ru.tinkoff.edu.java.scrapper.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcTgChatRepository tgChatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        tgChatRepository.add(1L);
        tgChatRepository.add(2L);
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldAddLink() {
        Long tgChatId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();

        Long linkId = linkRepository.add(linkUrl, tgChatId, updatedAt);

        Link addedLink = get(linkId);
        assertAll("Assert added link",
                () -> assertEquals(linkUrl, addedLink.getUrl()),
                () -> assertEquals(updatedAt.getHour(), addedLink.getUpdatedAt().getHour()),
                () -> assertTrue(existLinkChat(linkId, tgChatId))
        );
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldReturnIdExistingLink_whenLinkExist() {
        Long tgChatIdInDB = 1L;
        Long tgChatIdToAdd = 2L;
        Long linkIdInDB = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now(ZoneOffset.UTC);
        add(linkUrl, linkIdInDB, tgChatIdInDB, updatedAt, lastCheckAt);

        Long actualLinkId = linkRepository.add(linkUrl, tgChatIdToAdd, updatedAt);

        Link actualLink = get(actualLinkId);
        assertAll("Assert added link",
                () -> assertEquals(linkIdInDB, actualLink.getId()),
                () -> assertEquals(linkUrl, actualLink.getUrl()),
                () -> assertEquals(updatedAt.getHour(), actualLink.getUpdatedAt().getHour()),
                () -> assertEquals(lastCheckAt.getHour(), actualLink.getLastCheckAt().getHour()),
                () -> assertTrue(existLinkChat(actualLinkId, tgChatIdToAdd)),
                () -> assertTrue(existLinkChat(tgChatIdInDB, linkIdInDB))
        );
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldThrowException_whenLinkAndChatIdExist() {
        Long tgChatId = 1L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl, linkId, tgChatId, updatedAt, lastCheckAt);

        Throwable thrown = assertThrows(EmptyResultDataAccessException.class,
                () -> linkRepository.add(linkUrl, tgChatId, updatedAt));

        assertNotNull(thrown.getMessage());
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldDeleteLink_whenDeletedLastRelationWithLink() {
        Long tgChatId = 1L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl, linkId, tgChatId, updatedAt, lastCheckAt);

        Long deletedLinkId = linkRepository.remove(linkUrl, tgChatId);

        assertAll("Assert removed link",
                () -> assertEquals(linkId, deletedLinkId),
                () -> assertFalse(existLinkChat(deletedLinkId, tgChatId)),
                () -> assertEquals(0, getAll().size())
        );
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldDeleteRelationWithChat_whenThereAreOtherRelationWithLink() {
        Long tgChatId1 = 1L;
        Long tgChatId2 = 2L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now(ZoneOffset.UTC);
        add(linkUrl, linkId, tgChatId1, updatedAt, lastCheckAt);
        linkRepository.add(linkUrl, tgChatId2, updatedAt);

        Long actualLinkId = linkRepository.remove(linkUrl, tgChatId1);

        Link actualLink = get(actualLinkId);
        assertAll("Assert removed link",
                () -> assertEquals(linkId, actualLink.getId()),
                () -> assertEquals(linkUrl, actualLink.getUrl()),
                () -> assertEquals(updatedAt.getHour(), actualLink.getUpdatedAt().getHour()),
                () -> assertEquals(lastCheckAt.getHour(), actualLink.getLastCheckAt().getHour()),
                () -> assertTrue(existLinkChat(actualLinkId, tgChatId2)),
                () -> assertFalse(existLinkChat(actualLinkId, tgChatId1)),
                () -> assertEquals(1, getAll().size())
        );
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldThrowException_whenLinkAndChatIdNotExist() {
        Long tgChatId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";

        Throwable thrown = assertThrows(EmptyResultDataAccessException.class,
                () -> linkRepository.remove(linkUrl, tgChatId));

        assertNotNull(thrown.getMessage());
    }

    @Transactional
    @Rollback
    @Test
    void update_shouldUpdateLink() {
        Long tgChatId = 1L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        OffsetDateTime oldUpdatedAt = OffsetDateTime.of(
                2023, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime newUpdatedAt = OffsetDateTime.of(
                2023, 3, 25, 0, 0, 0, 0, ZoneOffset.UTC);
        add(linkUrl, linkId, tgChatId, oldUpdatedAt, lastCheckAt);

        Link actualLink = linkRepository.update(linkId, newUpdatedAt);

        assertAll("Assert updated link",
                () -> assertEquals(linkUrl, actualLink.getUrl()),
                () -> assertEquals(linkId, actualLink.getId()),
                () -> assertTrue(existLinkChat(linkId, tgChatId)),
                () -> assertEquals(newUpdatedAt, actualLink.getUpdatedAt()),
                () -> assertTrue(actualLink.getLastCheckAt().isAfter(OffsetDateTime.now().minusMinutes(1L)))
        );
    }

    @Transactional
    @Rollback
    @Test
    void updateLastCheck_shouldUpdateTimeOfLastLinkCheck() {
        Long tgChatId = 1L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime oldLastCheckAt = OffsetDateTime.of(
                2022, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC);
        add(linkUrl, linkId, tgChatId, updatedAt, oldLastCheckAt);

        linkRepository.updateLastCheck(linkId);

        Link actualLink = get(linkId);
        assertAll("Assert added link",
                () -> assertEquals(linkUrl, actualLink.getUrl()),
                () -> assertEquals(linkId, actualLink.getId()),
                () -> assertTrue(existLinkChat(linkId, tgChatId)),
                () -> assertTrue(actualLink.getLastCheckAt().isAfter(OffsetDateTime.now().minusSeconds(1)))
        );
    }

    @Transactional
    @Rollback
    @Test
    void getLinkId_shouldReturnLinkId() {
        Long tgChatId = 1L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl, linkId, tgChatId, updatedAt, lastCheckAt);

        Long actualLinkId = linkRepository.getLinkId(linkUrl);

        Link actualLink = get(actualLinkId);
        assertAll("Assert returned link",
                () -> assertEquals(linkId, actualLink.getId()),
                () -> assertEquals(linkUrl, actualLink.getUrl()),
                () -> assertEquals(updatedAt.getHour(), actualLink.getUpdatedAt().getHour()),
                () -> assertTrue(existLinkChat(linkId, tgChatId))
        );
    }

    @Transactional
    @Rollback
    @Test
    void getChatsId_shouldReturnAllChatsIdRelationWithLink() {
        Long tgChatId1 = 1L;
        Long tgChatId2 = 2L;
        Long linkId = 1L;
        String linkUrl = "https://github.com/MaxZhukoff/tinkoff-bot";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl, linkId, tgChatId1, updatedAt, lastCheckAt);
        linkRepository.add(linkUrl, tgChatId2, updatedAt);

        List<Long> chatIds = linkRepository.getChatsId(linkId);

        assertAll("Assert chats ids",
                () -> assertEquals(tgChatId1, chatIds.get(0)),
                () -> assertEquals(tgChatId2, chatIds.get(1)),
                () -> assertTrue(existLinkChat(linkId, tgChatId1)),
                () -> assertTrue(existLinkChat(linkId, tgChatId2)),
                () -> assertEquals(2, chatIds.size())
        );
    }

    @Transactional
    @Rollback
    @Test
    void findAllLinkId_shouldReturnAllLinksByChatId() {
        Long tgChatId = 1L;
        Long linkId1 = 1L;
        Long linkId2 = 2L;
        String linkUrl1 = "https://github.com/MaxZhukoff/tinkoff-bot";
        String linkUrl2 = "https://github.com/MaxZhukoff/Catalog";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl1, linkId1, tgChatId, updatedAt, lastCheckAt);
        add(linkUrl2, linkId2, tgChatId, updatedAt, lastCheckAt);

        List<LinkResponse> links = linkRepository.findAllLinkId(tgChatId);

        assertAll("Assert chats ids",
                () -> assertEquals(linkUrl1, links.get(0).url().toString()),
                () -> assertEquals(linkId1, links.get(0).id()),
                () -> assertEquals(linkUrl2, links.get(1).url().toString()),
                () -> assertEquals(linkId2, links.get(1).id()),
                () -> assertTrue(existLinkChat(linkId1, tgChatId)),
                () -> assertTrue(existLinkChat(linkId2, tgChatId)),
                () -> assertEquals(2, links.size())
        );
    }

    @Transactional
    @Rollback
    @Test
    void findAllLinksNotUpdatedAt_shouldReturnAllUnUpdatedLinks() {
        Long tgChatId = 1L;
        Long linkId1 = 1L;
        Long linkId2 = 2L;
        String linkUrl1 = "https://github.com/MaxZhukoff/tinkoff-bot";
        String linkUrl2 = "https://github.com/MaxZhukoff/Catalog";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl1, linkId1, tgChatId, updatedAt, lastCheckAt.minusMinutes(2L));
        add(linkUrl2, linkId2, tgChatId, updatedAt, lastCheckAt);

        List<Link> links = linkRepository.findAllLinksNotUpdatedAt(1L);

        assertEquals(1, links.size());
    }

    @Transactional
    @Rollback
    @Test
    void findAll_shouldReturnAllLinks() {
        Long tgChatId = 1L;
        Long linkId1 = 1L;
        Long linkId2 = 2L;
        String linkUrl1 = "https://github.com/MaxZhukoff/tinkoff-bot";
        String linkUrl2 = "https://github.com/MaxZhukoff/Catalog";
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckAt = OffsetDateTime.now();
        add(linkUrl1, linkId1, tgChatId, updatedAt, lastCheckAt);
        add(linkUrl2, linkId2, tgChatId, updatedAt, lastCheckAt);

        List<Link> links = linkRepository.findAll();

        assertEquals(2, links.size());
    }

    private Link get(Long linkId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM link WHERE id = ?",
                new BeanPropertyRowMapper<>(Link.class),
                linkId
        );
    }

    private List<Link> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM link",
                new BeanPropertyRowMapper<>(Link.class)
        );
    }

    private Link add(String url, Long linkId, Long tgChatId, OffsetDateTime updatedAt, OffsetDateTime lastCheckAt) {
        Link link = jdbcTemplate.queryForObject(
                "INSERT INTO link (id, url, updated_at, last_check_at) " +
                "VALUES (?, ?, ?, ? AT TIME ZONE 'utc') " +
                "RETURNING *", new BeanPropertyRowMapper<>(Link.class),
                linkId, url, updatedAt, lastCheckAt);

        jdbcTemplate.update("INSERT INTO link_chat (link_id, chat_id) VALUES (?, ?)",
                linkId, tgChatId);

        return link;
    }

    private Boolean existLinkChat(Long linkId, Long tgChatId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT link_id, chat_id FROM link_chat WHERE link_id = ? AND chat_id = ?)",
                Boolean.class,
                linkId, tgChatId
        );
    }
}
