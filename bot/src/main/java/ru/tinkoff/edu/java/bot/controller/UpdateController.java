package ru.tinkoff.edu.java.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
        @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))
})
@RestController
@RequestMapping("/updates")
public class UpdateController {
    @Operation(summary = "Отправить обновление")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    @PostMapping
    public ResponseEntity<Void> sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        return ResponseEntity.status(501).build();
    }
}
