package com.duck.myboard.controller;

import com.duck.myboard.exception.BoardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(BoardException.class)
    public String boardException(BoardException e) {
        int stCode = e.getStatusCode();
        String message = e.getMessage();

        log.error(">>>>>>>>>>>>>>>>>>> BoardException : code = " + stCode + " , message = " + message);

        return "redirect:error";
    }


}
