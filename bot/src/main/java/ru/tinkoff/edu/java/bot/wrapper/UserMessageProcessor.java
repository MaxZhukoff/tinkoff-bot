package ru.tinkoff.edu.java.bot.wrapper;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.wrapper.command.Command;
import ru.tinkoff.edu.java.bot.wrapper.command.ReplyCommand;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Component
public class UserMessageProcessor {
    private final List<Command> commands;

    SendMessage process(Update update) {
        Optional<Command> maybeCommand = commands.stream()
                .filter(command -> command.supports(update) || checkReplyCommand(command, update))
                .findFirst();
        if (maybeCommand.isEmpty()) {
            return new SendMessage(update.message().chat().id(),
                    "Неизвестная команда\nДля получения списка команда напишите /help");
        }
        return maybeCommand.get().handle(update);
    }

    boolean checkReplyCommand(Command command, Update update) {
        try {
            ReplyCommand replyCommand = (ReplyCommand) command;
            return replyCommand.canProcessReply(update);
        } catch (Exception e) {
            return false;
        }
    }
}
