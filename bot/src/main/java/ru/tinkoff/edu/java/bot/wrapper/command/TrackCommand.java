package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.AddLinkRequest;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command, ReplyCommand {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки";
    }

    @Override
    public String replyText() {
        return "Введите ссылку для отслеживания:";
    }

    @Override
    public SendMessage requestReply(Update update) {
        return new SendMessage(update.message().chat().id(), replyText())
                .replyMarkup(new ForceReply());
    }

    @Override
    public SendMessage handle(Update update) {
        if (!canProcessReply(update)) {
           return requestReply(update);
        }

        Long id = update.message().chat().id();
        String link = update.message().text();
        try {
            URI uriLink = URI.create(link);
            scrapperClient.fetchAddLinkTracking(id, new AddLinkRequest(uriLink));
        } catch (Exception ex) {
            return new SendMessage(id, "При добавлении ссылки в список отслеживаемых произошла ошибка");
        }
        return new SendMessage(id, "Ссылка успешно добавлена в список отслеживаемых");
    }
}
