package com.duck.myboard.controller;

import com.duck.myboard.common.ImageNameParser;
import com.duck.myboard.common.ImageProcess;
import com.duck.myboard.common.ResponseEntityUtil;
import com.duck.myboard.domain.Board;
import com.duck.myboard.exception.BlankException;
import com.duck.myboard.exception.BoardSaveException;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.request.ImgRequestTest;
import com.duck.myboard.response.BoardResponse;
import com.duck.myboard.response.ImageResponse;
import com.duck.myboard.service.BoardService;
import com.duck.myboard.validation.BlankValidation;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BlankValidation blankValidation;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @GetMapping("/boards/insert")
    public String insertBoard() {
        return "insert";
    }

    @GetMapping("/boards")
    public String getListBoard(Model model) {
        List<Board> boardList = boardService.getPagingList();
        model.addAttribute("boardList", boardList);
        return "index";
    }

    @PostMapping("/boards")
    public String writeBoard(@ModelAttribute BoardRequest boardRequest) { //todo 예외처리 바꿔야함
        blankValidation.isValid(boardRequest, "title", "content", "author");
        log.info("board Reqeust >>>>>>>>>>>>>>>>>>>>>> {}", boardRequest.getContent());

        ImageProcess imageProcess = new ImageProcess(boardRequest.getContent()); //이미지 처리
        boardRequest.setContent(imageProcess.getContent()); 

        Long boardId = boardService.write(boardRequest, imageProcess.getImageList());

        if(boardId == null) {
            throw new BoardSaveException();
        }

        return "redirect:/";
    }

    @PostMapping("/boards/{boardId}/update") //todo 구조 어떻게 바꿀지 생각
    public String editBoard(@PathVariable (value = "boardId") Long boardId, @ModelAttribute BoardRequest boardRequest) {
        blankValidation.isValid(boardRequest, "title", "content");
        boardRequest.setId(boardId);
        List<String> savedList = boardService.getImagePath(boardRequest.getId());
        ImageProcess imageProcess = new ImageProcess(boardRequest.getContent(), savedList); //이미지 처리
        boardRequest.setContent(imageProcess.getContent()); //이미지 처리 후 내용에 셋

        boardService.edit(boardRequest, imageProcess.getImageList(), imageProcess.getDeletePath());

        return "redirect:/";
    }

    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable(value = "boardId") Long boardId) {
        int result = boardService.delete(boardId);

        return "redirect:/";
    }

    @GetMapping("/boards/{boardId}")
    public String selectBoard(@PathVariable(value = "boardId") Long boardId, Model model) {
        BoardResponse boardResponse = boardService.get(boardId);
        log.info(">>>>>>>>>>>>>>>>> {}" , boardResponse.getContent());
        model.addAttribute("board", boardResponse);
        return "select";
    }

    @GetMapping("/imgtest")
    public String imgtest() {

        return "insert";
    }

    @PostMapping("/image/temp")  
    public ResponseEntity<?> imageTemp(MultipartFile file) { //에디터 이미지 임시저장
        if(file.isEmpty()) {
            return ResponseEntityUtil.empty("파일");
        } else if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            return ResponseEntityUtil.empty("파일");
        }

        Path tempDirPath = Path.of("./temp/image");
        try {
            if (!Files.exists(tempDirPath)) {
                Files.createDirectories(tempDirPath);
            }
            String originName = file.getOriginalFilename();
            String uuidName = UUID.randomUUID().toString();
            String tempName = uuidName + "_" + originName;
            Path tempFilePath = tempDirPath.resolve(tempName); // 패스 합치기
            //temp에 이미지 저장
            Files.copy(file.getInputStream(), tempFilePath);

            String imageUrl = "/temp/image/" + tempName; // 이미지 받을 주소

            return ResponseEntity.ok().body(Map.of("url", imageUrl));
            
        } catch (IOException e) {
            return ResponseEntityUtil.uploadFail();
        }

    }

    @GetMapping("/temp/image/{filename}") //임시 저장한 이미지 에디터로 보내는 주소
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path file = Path.of("./temp/image", filename); // 임시 저장 이미지파일 경로
        Resource resource = new UrlResource(file.toUri());
        log.info(">>>>>>>>>>>>>>>>> resource : {}", resource);

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mytest")
    public String updatetest(Model model) {
        BoardResponse board = boardService.get(20028L);
        log.info("board >>>>>>>>>>>> {}", board);

        model.addAttribute("board", board);
        return "update";
    }



//    private MediaType getMediaTypeForFileName(String fileName) {
//        if (fileName.endsWith(".png")) {
//            return MediaType.IMAGE_PNG;
//        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
//            return MediaType.IMAGE_JPEG;
//        } else if (fileName.endsWith(".gif")) {
//            return MediaType.IMAGE_GIF;
//        } else {
//            return MediaType.APPLICATION_OCTET_STREAM;
//        }
//    }
}
