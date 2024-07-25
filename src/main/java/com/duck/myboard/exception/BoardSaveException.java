package com.duck.myboard.exception;

public class BoardSaveException extends BoardException{

    private static String MESSAGE = "글 저장에 실패했습니다";

    public BoardSaveException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
