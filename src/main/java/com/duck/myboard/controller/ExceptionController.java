package com.duck.myboard.controller;

import com.duck.myboard.exception.BoardException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(BoardException.class)
    public String boardException(BoardException e) {
        int stCode = e.getStatusCode();
        String message = e.getMessage();

        return "redirect:error";
    }
}
