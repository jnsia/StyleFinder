package com.d111.backend.exception.user;

import org.springframework.http.ResponseEntity;

public class PasswordNotMatchException extends RuntimeException {

    public PasswordNotMatchException(String message) { super(message); }


}
