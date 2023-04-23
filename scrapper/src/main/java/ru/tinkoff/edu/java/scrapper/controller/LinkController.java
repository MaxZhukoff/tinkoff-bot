package ru.tinkoff.edu.java.scrapper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.controller.*;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
        @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/links")
public class LinkController {
    private final LinkService linkService;

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены", content = {
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ListLinksResponse.class))
    })
    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllTrackedLinks(@RequestHeader(name = "Tg-Chat-Id") Long id) {
        linkService.getAllTrackedLinks(id);
        return ResponseEntity.status(200).body(linkService.getAllTrackedLinks(id));
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена", content = {
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LinkResponse.class))
    })
    @PostMapping
    public ResponseEntity<LinkResponse> addLinkTracking(
            @RequestHeader(name = "Tg-Chat-Id") Long id,
            @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        return ResponseEntity.status(200).body(linkService.add(addLinkRequest, id));
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LinkResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Ссылка не найдена", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLinkTracking(
            @RequestHeader(name = "Tg-Chat-Id") Long id,
            @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        return ResponseEntity.status(200).body(linkService.remove(removeLinkRequest, id));
    }
}
