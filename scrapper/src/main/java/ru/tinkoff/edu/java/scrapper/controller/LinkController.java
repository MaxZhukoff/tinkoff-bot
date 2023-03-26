package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.*;

@RestController
@RequestMapping("/links")
public class LinkController {
    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllTrackedLinks(@RequestHeader(name = "Tg-Chat-Id") Long id) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLinkTracking(
            @RequestHeader(name = "Tg-Chat-Id") Long id,
            @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLinkTracking(
            @RequestHeader(name = "Tg-Chat-Id") Long id,
            @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        return ResponseEntity.status(501).build();
    }
}
