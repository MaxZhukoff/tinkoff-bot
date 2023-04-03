package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.RemoveLinkRequest;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class UntrackCommand implements Command, ReplyCommand {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public String replyText() {
        return "Введите ссылку для прекращения отслеживания:";
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
            scrapperClient.fetchRemoveLinkTracking(id, new RemoveLinkRequest(uriLink));
        } catch (Exception ex) {
            return new SendMessage(update.message().chat().id(), "При удалении ссылки из списка отслеживаемых произошла ошибка");
        }
        return new SendMessage(update.message().chat().id(), "Ссылка успешно убрана из списка отслеживаемых");
    }
}
