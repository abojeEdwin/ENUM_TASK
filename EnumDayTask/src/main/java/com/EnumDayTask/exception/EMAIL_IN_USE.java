package com.EnumDayTask.exception;

public class EMAIL_IN_USE extends RuntimeException {
    public EMAIL_IN_USE(String message) {
        super(message);
    }
}
