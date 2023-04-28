package ru.tinkoff.edu.java.scrapper.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link(
                rs.getLong("id"),
                rs.getString("url"),
                OffsetDateTime.of(rs.getTimestamp("updated_at").toLocalDateTime(), ZoneOffset.UTC),
                OffsetDateTime.of(rs.getTimestamp("last_check_at").toLocalDateTime(), ZoneOffset.UTC),
                rs.getTimestamp("last_commit_at") != null
                        ? OffsetDateTime.of(rs.getTimestamp("last_commit_at").toLocalDateTime(), ZoneOffset.UTC)
                        : null,
                rs.getInt("issues_count"),
                rs.getInt("answer_count"),
                null
        );
    }
}