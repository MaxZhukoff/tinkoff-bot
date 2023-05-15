package ru.tinkoff.edu.java.scrapper.handler;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.dto.controller.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ApiErrorResponse(
            "Некорректные параметры запроса",
            String.valueOf(e.getStatusCode().value()),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(Objects::toString).toList()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ResponseStatusException e) {
        return new ResponseEntity<>(new ApiErrorResponse(
            e.getReason(),
            e.getStatusCode().toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(Objects::toString).toList()
        ), e.getStatusCode());
    }
}
