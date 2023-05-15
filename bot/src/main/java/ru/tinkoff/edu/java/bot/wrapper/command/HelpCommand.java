package ru.tinkoff.edu.java.bot.wrapper.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
        this.commands.add(this);
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder sb = new StringBuilder();
        commands.forEach(command -> sb.append(String.format("%s%n", command.toApiCommand())));
        return new SendMessage(update.message().chat().id(), sb.toString());
    }
}
