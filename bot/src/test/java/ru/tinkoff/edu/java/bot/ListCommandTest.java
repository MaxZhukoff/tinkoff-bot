package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.wrapper.command.ListCommand;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    @Mock
    ScrapperClient scrapperClient;

    @Mock
    Update mockUpdate;

    @Mock
    Message mockMessage;

    @Mock
    Chat mockChat;

    @Test
    void handle_shouldReturnSpecialMessageTest_whenResponseLinksListEmpty() {
        //given
        Long chatId = 1L;
        when(mockUpdate.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.chat().id()).thenReturn(chatId);
        when(scrapperClient.fetchGetAllTrackedLinks(chatId)).thenReturn(new ListLinksResponse(Collections.emptyList(), 0));
        ListCommand command = new ListCommand(scrapperClient);

        //when
        SendMessage result = command.handle(mockUpdate);

        //then
        String expectedResult = String.format("%s%n%s",
                "Вы не отслеживаете ни одной ссылки!",
                "Для добавление ссылки в список отслеживаемых напишите /track");
        assertThat(result.getParameters().get("text")).isEqualTo(expectedResult);
    }

    @Test
    void handle_shouldReturnAllTrackedLinks() {
        //given
        Long chatId = 1L;
        when(mockUpdate.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockMessage.chat().id()).thenReturn(chatId);
        List<LinkResponse> links = List.of(
                new LinkResponse(chatId, URI.create("https://github.com/MaxZhukoff/tinkoff-bot")),
                new LinkResponse(chatId, URI.create("https://github.com/MaxZhukoff/web-programing"))
        );
        when(scrapperClient.fetchGetAllTrackedLinks(chatId)).thenReturn(new ListLinksResponse(links, links.size()));
        ListCommand command = new ListCommand(scrapperClient);

        //when
        SendMessage result = command.handle(mockUpdate);

        //then
        String expectedResult = "Список ваших отслеживаемых ссылок:\n" + String.format("%s%n%s%n",
                "https://github.com/MaxZhukoff/tinkoff-bot",
                "https://github.com/MaxZhukoff/web-programing");
        assertThat(result.getParameters().get("text")).isEqualTo(expectedResult);
    }
}
