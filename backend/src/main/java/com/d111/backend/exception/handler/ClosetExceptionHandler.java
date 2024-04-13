package com.d111.backend.exception.handler;

import com.d111.backend.exception.closet.ClosetImageIOException;
import com.d111.backend.exception.closet.ClosetNotFoundException;
import com.d111.backend.exception.comment.CommentNotFoundException;
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
public class ClosetExceptionHandler {

    private final Gson gson;

    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(ClosetNotFoundException.class)
    public ResponseEntity<String> handleClosetNotFoundException(ClosetNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(ClosetImageIOException.class)
    public ResponseEntity<String> handleClosetImageIOException(ClosetImageIOException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    public String stringToJson(String message) { return gson.toJson(Collections.singletonMap("message", message)); }

}
