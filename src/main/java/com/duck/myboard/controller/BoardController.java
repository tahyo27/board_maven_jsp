package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.exception.BlankException;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.request.ImgRequestTest;
import com.duck.myboard.response.BoardResponse;
import com.duck.myboard.service.BoardService;
import com.duck.myboard.validation.BlankValidation;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BlankValidation blankValidation;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @GetMapping("/boards")
    public String getListBoard(Model model) {
        List<Board> boardList = boardService.getPagingList();
        model.addAttribute("boardList", boardList);
        return "index";
    }

    @PostMapping("/boards")
    public String writeBoard(@ModelAttribute BoardRequest boardRequest) {
        blankValidation.isValid(boardRequest, "title", "content", "author");
        int result = boardService.write(boardRequest);
        return "redirect:/";
    }

    @PatchMapping("/boards/{boardId}")
    public String editBoard(@PathVariable(value = "boardId") Long boardId, @ModelAttribute BoardRequest boardRequest) {
        blankValidation.isValid(boardRequest, "title", "content");
        log.info(">>>>>>>>>>>>>>>>>>>>>> edit board = {}", boardRequest);
        int result = boardService.edit(boardId, boardRequest);

        return "redirect:/";
    }

    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(@PathVariable(value = "boardId") Long boardId) {
        int result = boardService.delete(boardId);

        return "redirect:/";
    }

    @GetMapping("/boards/{boardId}")
    public String selectBoard(@PathVariable(value = "boardId") Long boardId, Model model) {
        BoardResponse boardResponse = boardService.get(boardId);
        model.addAttribute("board", boardResponse);
        return "select";
    }

    @GetMapping("/imgtest")
    public String imgtest() {

        return "imgtest";
    }

    @PostMapping("/imgtest")
    public String uploadtest(@ModelAttribute ImgRequestTest imgRequestTest) throws IOException {

        for(MultipartFile file : imgRequestTest.getImages()) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>> {}", file.getOriginalFilename());
        }

        return "";
    }
}
