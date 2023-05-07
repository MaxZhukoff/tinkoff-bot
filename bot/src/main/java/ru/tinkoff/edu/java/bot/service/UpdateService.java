package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.wrapper.BotUpdateListener;

@RequiredArgsConstructor
@Service
public class UpdateService {
    private final BotUpdateListener botUpdateListener;

    public void handleUpdate(LinkUpdateRequest linkUpdateRequest) {
        linkUpdateRequest.tgChatIds().forEach(
            tgChatId -> botUpdateListener.execute(
                new SendMessage(
                    tgChatId,
                    String.format("Ваша ссылка: %s обновилась%n%s",
                        linkUpdateRequest.url(), linkUpdateRequest.description()
                    )
                ).disableWebPagePreview(true)
            )
        );
    }
}
