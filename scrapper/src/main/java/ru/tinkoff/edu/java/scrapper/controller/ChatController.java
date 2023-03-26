package ru.tinkoff.edu.java.scrapper.controller;

import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tg-chat/{id}")
public class ChatController {
    @PostMapping
    public ResponseEntity<Void> registerChat(@PathVariable @Min(0) Long id) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteChat(@PathVariable @Min(0) Long id) {
        return ResponseEntity.status(501).build();
    }
}
