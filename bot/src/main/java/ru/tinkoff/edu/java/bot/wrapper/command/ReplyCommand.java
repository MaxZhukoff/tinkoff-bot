package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface ReplyCommand {
    default boolean canProcessReply(Update update) {
        return update.message().replyToMessage() != null
            && update.message().replyToMessage().text().equals(replyText());
    }

    String replyText();

    SendMessage requestReply(Update update);
}
