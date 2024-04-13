package com.d111.backend.exception.handler;

import com.d111.backend.exception.feed.CoordiNotFoundException;
import com.d111.backend.exception.feed.FeedImageIOException;
import com.d111.backend.exception.feed.FeedNotFoundException;
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
public class FeedExceptionHandler {

    private final Gson gson;

    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(FeedNotFoundException.class)
    public ResponseEntity<String> handleFeedNotFoundException(FeedNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(CoordiNotFoundException.class)
    public ResponseEntity<String> handleCoordiNotFoundException(CoordiNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    @ExceptionHandler(FeedImageIOException.class)
    public ResponseEntity<String> handleFeedImageIOException(FeedImageIOException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToJson(exception.getMessage()));
    }

    public String stringToJson(String message) { return gson.toJson(Collections.singletonMap("message", message)); }

}
