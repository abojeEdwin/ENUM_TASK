package com.EnumDayTask.exception;

public class TOKEN_EXPIRED extends RuntimeException {
    public TOKEN_EXPIRED(String message) {
        super(message);
    }
}
