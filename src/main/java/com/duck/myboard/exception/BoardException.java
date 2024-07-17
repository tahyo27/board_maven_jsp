package com.duck.myboard.exception;

public abstract class BoardException extends RuntimeException{

    public BoardException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
