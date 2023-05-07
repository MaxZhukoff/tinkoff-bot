package ru.tinkoff.edu.java.scrapper.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.mapper.ChatRowMapper;

@RequiredArgsConstructor
@Repository
public class JdbcTgChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ChatRowMapper chatRowMapper;

    public void add(Long id) {
        jdbcTemplate.update("""
            INSERT INTO chat(id)
            VALUES (?)
            ON CONFLICT DO NOTHING;
            """, id);
    }

    public void remove(Long id) {
        jdbcTemplate.queryForObject("""
            DELETE
            FROM chat
            WHERE id = ?
            RETURNING id;
            """, Long.class, id);
    }

    public List<Chat> findAll() {
        return jdbcTemplate.query("""
            SELECT id FROM CHAT
            """, chatRowMapper);
    }
}
