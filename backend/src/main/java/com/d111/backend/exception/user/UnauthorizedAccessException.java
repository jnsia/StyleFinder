package com.d111.backend.exception.user;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message) { super(message); }

}
