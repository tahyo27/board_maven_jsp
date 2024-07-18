package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.exception.BlankException;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.service.BoardService;
import com.duck.myboard.validation.BlankValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BlankValidation blankValidation;

    @GetMapping("/boards")
    public String getListBoard(Model model) {
        List<Board> boardList = boardService.getPagingList();
        model.addAttribute("boardList", boardList);
        return "index";
    }

    @PostMapping("/boards")
    public String writeBoard(@ModelAttribute BoardRequest boardRequest) {
        blankValidation.isValid(boardRequest);
        int result = boardService.write(boardRequest);
        return "redirect:/";
    }

    @PatchMapping("/boards/{boardId}")
    public String editBoard(@PathVariable(value = "boardId") Long boardId, @ModelAttribute BoardRequest boardRequest) {
        blankValidation.isValid(boardRequest);
        log.info(">>>>>>>>>>>>>>>>>>>>>> edit board = {}", boardRequest);
        int result = boardService.edit(boardId, boardRequest);

        return "redirect:/";
    }

    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(@PathVariable(value = "boardId") Long boardId) {
        int result = boardService.delete(boardId);

        return "redirect:/";
    }
}
