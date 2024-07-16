package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.request.BoardCreate;
import com.duck.myboard.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String getListBoard() {
        List<Board> boardList = boardService.getList();
        log.info(">>>>>>>>>>>>>>>>>>>>>> boardList = {}", boardList);

        return "";
    }

    @PostMapping("/boards")
    public String writeBoard(@ModelAttribute BoardCreate boardCreate) {
        int result = boardService.write(boardCreate);

        return "";
    }
}
