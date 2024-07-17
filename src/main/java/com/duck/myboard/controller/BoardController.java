package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.request.BoardCreate;
import com.duck.myboard.request.BoardEdit;
import com.duck.myboard.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String getListBoard(Model model) {
        List<Board> boardList = boardService.getPagingList();
        model.addAttribute("boardList", boardList);
        return "index";
    }

    @PostMapping("/boards")
    public String writeBoard(@ModelAttribute BoardCreate boardCreate) {
        int result = boardService.write(boardCreate);
        return "redirect:/";
    }

    @PatchMapping("/boards")
    public String editBoard(@ModelAttribute BoardEdit boardEdit) {
        log.info(">>>>>>>>>>>>>>>>>>>>>> edit board = {}", boardEdit);
        int result = boardService.edit(boardEdit);

        return "redirect:/";
    }

    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(@PathVariable(value = "boardId") Long boardId) {
        int result = boardService.delete(boardId);

        return "redirect:/";
    }
}
