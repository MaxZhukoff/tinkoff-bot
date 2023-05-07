package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaTgChatRepository tgChatRepository;

    @Override
    public void register(Long tgChatId) {
        Chat chat = new Chat();
        chat.setId(tgChatId);

        tgChatRepository.save(chat);
    }

    @Override
    public void unregister(Long tgChatId) {
        Chat chat = tgChatRepository.findById(tgChatId).orElseThrow(
            () -> new ResponseStatusException(NOT_FOUND, "Chat not found")
        );

        tgChatRepository.delete(chat);
    }
}
