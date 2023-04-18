package ru.tinkoff.edu.java.scrapper.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.Chat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class JdbcTgChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcTgChatRepository tgChatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Rollback
    @Test
    void add_shouldAddChatToDB() {
        Long tgChatId = 1L;

        tgChatRepository.add(tgChatId);

        assertEquals(get(tgChatId).getId(), tgChatId);
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldDoNothing_whenChatIdExistInDB() {
        Long tgChatId = 1L;
        add(tgChatId);

        tgChatRepository.add(tgChatId);

        assertAll("Assert response chat",
                () -> assertEquals(get(tgChatId).getId(), tgChatId),
                () -> assertEquals(getAll().size(), 1)
        );
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldDeleteChat() {
        Long tgChatId = 1L;
        add(tgChatId);

        tgChatRepository.remove(tgChatId);

        assertEquals(getAll().size(), 0);
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldThrowException_whenChatIdNotExist() {
        Long tgChatId = 1L;

        Throwable thrown = assertThrows(EmptyResultDataAccessException.class,
                () -> tgChatRepository.remove(tgChatId));

        assertNotNull(thrown.getMessage());
    }

    @Transactional
    @Rollback
    @Test
    void findAll_shouldReturnAllChats() {
        add(1L);
        add(2L);
        add(3L);

        List<Chat> chats = tgChatRepository.findAll();

        assertEquals(chats.size(), 3);
    }

    private Chat get(Long tgChatId) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM chat WHERE id = ?",
                new BeanPropertyRowMapper<>(Chat.class),
                tgChatId
        );
    }

    private List<Chat> getAll() {
        return jdbcTemplate.query(
                "SELECT id FROM chat",
                new BeanPropertyRowMapper<>(Chat.class)
        );
    }

    private void add(Long tgChatId) {
        jdbcTemplate.update("INSERT INTO chat VALUES (?)", tgChatId);
    }
}
