package com.EnumDayTask.exception;

public class INVALID_CREDENTIAL extends RuntimeException {
    public INVALID_CREDENTIAL(String message) {
        super(message);
    }
}
