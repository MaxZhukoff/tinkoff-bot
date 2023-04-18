package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.repository.JdbcTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Primary
@Service
public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository tgChatRepository;

    @Override
    public void register(Long tgChatId) {
        tgChatRepository.add(tgChatId);
    }

    @Override
    public void unregister(Long tgChatId) {
        try {
            tgChatRepository.remove(tgChatId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(NOT_FOUND, "Chat not found");
        }

    }
}
