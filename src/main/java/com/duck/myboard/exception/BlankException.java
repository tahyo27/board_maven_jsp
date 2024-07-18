package com.duck.myboard.exception;

public class BlankException extends BoardException {
    private static final String MESSAGE = "값이 필요합니다";

    public BlankException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
