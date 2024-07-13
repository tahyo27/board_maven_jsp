package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String board() {
        log.info(">>>>>>>>>>>>>>>>>>>>>> board로 들어옴");
        List<Board> boardList = boardService.getList();
        log.info(">>>>>>>>>>>>>>>>>>>>>> boardList = {}", boardList);

        return "";
    }
}
