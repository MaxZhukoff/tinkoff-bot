package ru.tinkoff.edu.java.bot.wrapper;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BotUpdateListener implements AutoCloseable, UpdatesListener {
    private final TelegramBot bot;
    private final UserMessageProcessor userMessageProcessor;

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream().map(userMessageProcessor::process).forEach(this::execute);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SetMyCommands buildSetCommandsRequest() {
        BotCommand[] botCommands = userMessageProcessor.getCommands().stream()
                .map(command -> new BotCommand(command.command(), command.description()))
                .toArray(BotCommand[]::new);
        return new SetMyCommands(botCommands);
    }

    @PostConstruct
    public void start() {
        bot.setUpdatesListener(this);
        bot.execute(buildSetCommandsRequest());
    }

    @Override
    @PreDestroy
    public void close() {
        bot.shutdown();
    }
}
