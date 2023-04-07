package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        List<LinkResponse> links = scrapperClient.fetchGetAllTrackedLinks(chatId).links();

        String messageText;
        if (links.isEmpty()) {
            messageText = String.format("%s%n%s",
                    "Вы не отслеживаете ни одной ссылки!",
                    "Для добавление ссылки в список отслеживаемых напишите /track");
        } else {
            StringBuilder sb = new StringBuilder("Список ваших отслеживаемых ссылок:\n");
            links.stream().map(LinkResponse::url).forEach(link -> sb.append(String.format("%s%n", link)));
            messageText = sb.toString();
        }

        return new SendMessage(chatId, messageText).disableWebPagePreview(true);
    }
}
