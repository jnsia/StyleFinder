package com.d111.backend.exception.user;

public class ExistedEmailException extends RuntimeException {

    public ExistedEmailException(String message) { super(message); }

}
