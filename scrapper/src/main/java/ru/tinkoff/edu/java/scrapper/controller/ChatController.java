package ru.tinkoff.edu.java.scrapper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.controller.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
        @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/tg-chat/{id}")
public class ChatController {
    private final TgChatService chatService;

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @PostMapping
    public ResponseEntity<Void> registerChat(@PathVariable @Min(0) Long id) {
        chatService.register(id);
        return ResponseEntity.status(200).build();
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Чат не существует", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteChat(@PathVariable @Min(0) Long id) {
        chatService.unregister(id);
        return ResponseEntity.status(200).build();
    }
}
