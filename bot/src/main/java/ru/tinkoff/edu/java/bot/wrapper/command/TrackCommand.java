package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command, ReplyCommand {
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
    public SendMessage replyHandle(Update update) {
        return new SendMessage(update.message().chat().id(), replyText())
                .replyMarkup(new ForceReply());
    }

    @Override
    public SendMessage handle(Update update) {
        if (!canProcessReply(update)) {
           return replyHandle(update);
        }
        String link = update.message().text();
        try {
            //TODO impl
        } catch (Exception ex) {
            return new SendMessage(update.message().chat().id(), "При добавлении ссылки в список отслеживаемых произошла ошибка");
        }
        return new SendMessage(update.message().chat().id(), "Ссылка успешно добавлена в список отслеживаемых");
    }
}
