package ru.tinkoff.edu.java.scrapper.repository;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.mapper.LinkResponseRowMapper;
import ru.tinkoff.edu.java.scrapper.repository.mapper.LinkRowMapper;

@RequiredArgsConstructor
@Repository
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkResponseRowMapper linkResponseRowMapper;
    private final LinkRowMapper linkRowMapper;

    private static final String FIND_ALL_LINKS = """
        SELECT * FROM link;
        """;

    private static final String INSERT_LINK = """
        WITH
            find_link AS (
                SELECT id FROM link WHERE url = ?
            ),
            insert_new_link AS (
                INSERT INTO link (url, updated_at, last_check_at, last_commit_at, issues_count, answer_count)
                    SELECT ?, ?, (NOW() AT TIME ZONE 'utc'), ?, ?, ?
                    WHERE NOT EXISTS (SELECT FROM find_link)
                    RETURNING id
            ),
            link_id AS (
                SELECT id FROM insert_new_link
                UNION ALL
                SELECT id FROM find_link
            )
        INSERT INTO link_chat(link_id, chat_id)
        SELECT id, ? FROM link_id
        ON CONFLICT DO NOTHING
        RETURNING link_id;
        """;

    private static final String REMOVE_LINK = """
        DELETE FROM link_chat
            USING link
            WHERE link_chat.link_id = link.id
                AND link.url = ?
                AND link_chat.chat_id = ?
            RETURNING link_id;
        """;

    private static final String GET_LINK_ID_BY_URL = """
        SELECT lc.link_id AS id
        FROM link_chat lc
                 JOIN link l ON lc.link_id = l.id
        WHERE l.url = ?
        """;

    private static final String GET_LINKS_BY_CHAT_ID = """
        SELECT lc.link_id AS id, l.url AS url
        FROM link_chat lc
                 JOIN link l ON lc.link_id = l.id
        WHERE lc.chat_id = ?;
        """;

    private static final String GET_LINKS_NOT_UPDATED = """
        SELECT id, url, updated_at, last_check_at, last_commit_at, issues_count, answer_count FROM link
        WHERE last_check_at < ((NOW() AT TIME ZONE 'utc') - ? * interval '1 minutes')::TIMESTAMP;
        """;

    private static final String UPDATE_LINK = """
        UPDATE link
        SET updated_at = ? AT TIME ZONE 'utc',
        last_check_at = (NOW() AT TIME ZONE 'utc'),
        last_commit_at = ?,
        issues_count = ?,
        answer_count = ?
            WHERE id = ?
        RETURNING id, url, updated_at, last_check_at, last_commit_at, issues_count, answer_count;
        """;

    private static final String UPDATE_TIME_OF_LAST_CHECK = """
        UPDATE link SET last_check_at = (NOW() AT TIME ZONE 'utc')
            WHERE id = ?
        """;

    private static final String GET_CHAT_ID_BY_LINK_ID = """
        SELECT chat_id
        FROM link_chat
        WHERE link_id = ?;
        """;

    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_LINKS, linkRowMapper);
    }

    public Long add(
        String url, Long tgChatId, OffsetDateTime updatedAt,
        OffsetDateTime lastCommitAt, Integer issuesCount, Integer answerCount
    ) {
        return jdbcTemplate.queryForObject(INSERT_LINK, Long.class,
            url, url, updatedAt, lastCommitAt, issuesCount, answerCount, tgChatId
        );
    }

    public Long remove(String url, Long tgChatId) {
        return jdbcTemplate.queryForObject(REMOVE_LINK, Long.class, url, tgChatId);
    }

    public Link update(
        Long id, OffsetDateTime updatedAt,
        OffsetDateTime lastCommitAt, Integer issuesCount, Integer answerCount
    ) {
        return jdbcTemplate.queryForObject(UPDATE_LINK, linkRowMapper,
            updatedAt, lastCommitAt, issuesCount, answerCount, id
        );
    }

    public void updateLastCheck(Long id) {
        jdbcTemplate.update(UPDATE_TIME_OF_LAST_CHECK, id);
    }

    public Long getLinkId(String url) {
        return jdbcTemplate.queryForObject(GET_LINK_ID_BY_URL, Long.class, url);
    }

    public List<Long> getChatsId(Long linkId) {
        return jdbcTemplate.query(GET_CHAT_ID_BY_LINK_ID, (rs, rowNum) ->
            rs.getLong("chat_id"), linkId);
    }

    public List<LinkResponse> findAllLinkId(Long tgChatId) {
        return jdbcTemplate.query(GET_LINKS_BY_CHAT_ID, linkResponseRowMapper, tgChatId);
    }

    public List<Link> findAllLinksNotUpdatedAt(Long minutes) {
        return jdbcTemplate.query(GET_LINKS_NOT_UPDATED, linkRowMapper, minutes);
    }
}
