package com.EnumDayTask.exception;

public class RATE_LIMITED extends RuntimeException {
    public RATE_LIMITED(String message) {
        super(message);
    }
}
