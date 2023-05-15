package ru.tinkoff.edu.java.scrapper.repository.mapper;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;

@Component
public class LinkResponseRowMapper implements RowMapper<LinkResponse> {
    @Override
    public LinkResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LinkResponse(
            rs.getLong("id"),
            URI.create(rs.getString("url"))
        );
    }
}
