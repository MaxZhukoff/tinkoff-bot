package ru.tinkoff.edu.java.bot.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    @PostMapping
    public ResponseEntity<Void> sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        return ResponseEntity.status(501).build();
    }
}
