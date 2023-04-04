package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.wrapper.UserMessageProcessor;
import ru.tinkoff.edu.java.bot.wrapper.command.*;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorTest {
    @Mock
    Command mockCommand;
    @Mock
    Update mockUpdate;
    @Mock
    Message mockMessage;
    @Mock
    Chat mockChat;

    @Test
    void process_shouldReturnUnknownCommandMessage_whenCommandNotFound() {
        //given
        Long chatId = 1L;
        when(mockUpdate.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.chat().id()).thenReturn(chatId);
        when(mockCommand.supports(mockUpdate)).thenReturn(false);
        UserMessageProcessor userMessageProcessor = new UserMessageProcessor(Collections.singletonList(mockCommand));

        //when
        SendMessage result = userMessageProcessor.process(mockUpdate);

        //then
        assertThat(result.getParameters().get("text")).isEqualTo(
                "Неизвестная команда\nДля получения списка команда напишите /help");
    }
}
