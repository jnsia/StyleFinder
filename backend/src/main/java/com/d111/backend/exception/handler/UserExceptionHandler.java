package com.d111.backend.exception.handler;

import com.d111.backend.exception.user.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler {

    private final Gson gson;

    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(ExistedEmailException.class)
    public ResponseEntity<String> handleExistedEmailException(ExistedEmailException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<String> handlePasswordNotMatchException(PasswordNotMatchException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(CustomJWTException.class)
    public ResponseEntity<String> handleCustomJWTException(CustomJWTException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(ProfileImageIOException.class)
    public ResponseEntity<String> handleProfileImageIOException(ProfileImageIOException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<String> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    public String stringToJson(String message) { return gson.toJson(Collections.singletonMap("message", message)); }

}
