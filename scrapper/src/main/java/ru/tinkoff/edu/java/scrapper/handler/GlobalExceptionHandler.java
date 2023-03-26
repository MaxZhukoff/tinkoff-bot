package ru.tinkoff.edu.java.scrapper.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;

import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler{
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

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ApiErrorResponse> handleException(HttpClientErrorException.NotFound e)  {
        return new ResponseEntity<>(new ApiErrorResponse(
                "Запрашиваемый объект не найден",
                String.valueOf(404),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(Objects::toString).toList()
        ), HttpStatus.NOT_FOUND);
    }
}
