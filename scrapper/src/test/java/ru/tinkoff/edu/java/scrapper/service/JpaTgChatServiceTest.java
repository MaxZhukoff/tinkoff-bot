package ru.tinkoff.edu.java.scrapper.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.Chat;
import ru.tinkoff.edu.java.scrapper.repository.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JpaTgChatServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaTgChatService tgChatService;
    @Autowired
    private JpaTgChatRepository tgChatRepository;

    @Transactional
    @Rollback
    @Test
    public void register_shouldSaveChat() {
        Long tgChatId = 12345L;

        tgChatService.register(tgChatId);

        Chat chat = tgChatRepository.findById(tgChatId).orElse(null);

        assertNotNull(chat);
        assertEquals(tgChatId, chat.getId());
    }

    @Transactional
    @Rollback
    @Test
    public void unregister_shouldRemoveChat() {
        Long tgChatId = 12345L;
        Chat chat = new Chat();
        chat.setId(tgChatId);
        tgChatRepository.save(chat);

        tgChatService.unregister(tgChatId);

        Chat deletedChat = tgChatRepository.findById(tgChatId).orElse(null);
        assertNull(deletedChat);
    }

    @Transactional
    @Rollback
    @Test
    public void unregister_shouldThrowException_whenChatNotFound() {
        Long tgChatId = 12345L;

        var notFoundException = assertThrows(ResponseStatusException.class,
                () -> tgChatService.unregister(tgChatId));

        assertEquals(notFoundException.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
