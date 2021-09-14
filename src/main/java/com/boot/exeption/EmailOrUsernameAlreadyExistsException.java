package com.boot.exeption;

public class EmailOrUsernameAlreadyExistsException extends RuntimeException {


    public EmailOrUsernameAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
