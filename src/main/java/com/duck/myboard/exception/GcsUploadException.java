package com.duck.myboard.exception;

public class GcsUploadException extends BoardException {

    private static final String MESSAGE = "구글 스토리지 업로드를 실패했습니다";



    public GcsUploadException() {
        super(MESSAGE);
    }

    public GcsUploadException(Throwable e) {
        super(MESSAGE, e);
    }


    @Override
    public int getStatusCode() {
        return 500;
    }
}
