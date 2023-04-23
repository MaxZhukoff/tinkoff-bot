package ru.tinkoff.edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.wrapper.BotUpdateListener;

@ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
        @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/updates")
public class UpdateController {
    private final BotUpdateListener botUpdateListener;

    @Operation(summary = "Отправить обновление")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    @PostMapping
    public ResponseEntity<Void> sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        linkUpdateRequest.tgChatIds().forEach(tgChatId -> botUpdateListener.execute(
                new SendMessage(
                        tgChatId,
                        String.format("Ваша ссылка: %s обновилась%n%s",
                                linkUpdateRequest.url(), linkUpdateRequest.description())
                ).disableWebPagePreview(true)
        ));
        return ResponseEntity.status(200).build();
    }
}
