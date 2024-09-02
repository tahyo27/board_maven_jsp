package com.duck.myboard.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity<String> empty(String name) {
        return ResponseEntity.badRequest().body(name + "이 비어있거나 잘못되었습니다.");
    }

    public static ResponseEntity<String> uploadFail() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드를 실패하였습니다.");
    }
}
