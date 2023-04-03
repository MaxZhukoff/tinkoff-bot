package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command, ReplyCommand {
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
            return new SendMessage(update.message().chat().id(), "При удалении ссылки из списка отслеживаемых произошла ошибка");
        }
        return new SendMessage(update.message().chat().id(), "Ссылка успешно убрана из списка отслеживаемых");
    }
}
