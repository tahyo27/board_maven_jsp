package com.duck.myboard.controller;

import com.duck.myboard.common.ImageNameParser;
import com.duck.myboard.common.ImageProcess;
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

        ImageProcess imageProcess = new ImageProcess(boardRequest.getContent());
//        List<ImageNameParser> imageList = new ArrayList<>();
//        Document doc = Jsoup.parse(boardRequest.getContent());
//        Elements images = doc.select("img");
//
//        if(!images.isEmpty()) {
//            for(Element image : images) {
//                String srcStr = image.attr("src");
//                ImageNameParser imageNameParser = new ImageNameParser(srcStr);
//                imageList.add(imageNameParser);
//                image.attr("src", imageNameParser.getGcsPath());
//            }
//        }
//
//        String updatedContent = doc.body().html();
//        log.info("board Reqeust changed >>>>>>>>>>>>>>>>>>>>>> {}", updatedContent);
        boardRequest.setContent(imageProcess.getContent()); //이미지 주소 바꿔서 세팅

        Long boardId = boardService.write(boardRequest, imageProcess.getImageList());

        if(boardId == null) {
            throw new BoardSaveException();
        }

        return "redirect:/";
    }

    @PostMapping("/boards/{boardId}/update") //todo 구조 어떻게 바꿀지 생각
    public String editBoard(@PathVariable (value = "boardId") Long boardId, @ModelAttribute BoardRequest boardRequest) {
        blankValidation.isValid(boardRequest, "title", "content"); //todo 이미지 수정 때 어떻게 할지 고민 /image/temp 로 시작하는 것만 바꾸기
        boardRequest.setId(boardId);
        log.info(">>>>>>>>>>>>>board edit {} ", boardRequest);
        List<String> savedList = boardService.getImagePath(boardRequest.getId());
        log.info(">>>>>>>>>>>>>>>>>>>>savedList {}", savedList);
        //image에서 저장되어있는 gcspath 불러와서 비교후 지워야 하는거 필터링 해아할듯
        Document doc = Jsoup.parse(boardRequest.getContent());
        Elements images = doc.select("img");

        List<ImageNameParser> imageList = new ArrayList<>(); //todo 이미지 없을때 처리 생각
        List<String> existList = new ArrayList<>();
        if(!images.isEmpty()) {
            for(Element image : images) { //todo write랑 중복되는 부분 나중에 묶을 생각
                String srcStr = image.attr("src");
                if(srcStr.startsWith("/temp/image")) {
                    log.info(">>>>>>>>>> contains >>>>>>>>> srcStr : {}", srcStr);
                    ImageNameParser imageNameParser = new ImageNameParser(srcStr);
                    imageList.add(imageNameParser);
                    image.attr("src", imageNameParser.getGcsPath());
                } else if(srcStr.startsWith("https://storage.googleapis.com/imgtest_bucket")) {
                    existList.add(srcStr);
                }
            }
        }

        List<String> deletePath = savedList.stream()
                .filter(item -> !existList.contains(item)).toList();

        //넘겨서 지우고 추가

        log.info(">>>>>>>>>>>>>>>>>>>>>> deletePath {}", deletePath);

        String updatedContent = doc.body().html();
        log.info("board Reqeust changed >>>>>>>>>>>>>>>>>>>>>> {}", updatedContent);
        boardRequest.setContent(updatedContent); //이미지 주소 바꿔서 세팅
        boardService.edit(boardRequest, imageList, deletePath);

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
            return ResponseEntity.badRequest().body("파일이 업로드되지 않았습니다");
        } else if (file.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().body("파일의 이름이 잘못되었습니다");
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
            return ResponseEntity.status(500).body("이미지 업로드 실패");
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

    private String imageProcess(String content) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        List<ImageNameParser> imageList = new ArrayList<>(); //todo 이미지 없을때 처리 생각
        List<String> existList = new ArrayList<>();

        if(!images.isEmpty()) {
            for(Element image : images) { //todo write랑 중복되는 부분 나중에 묶을 생각
                String srcStr = image.attr("src");
                if(srcStr.startsWith("/temp/image")) {
                    log.info(">>>>>>>>>> contains >>>>>>>>> srcStr : {}", srcStr);
                    ImageNameParser imageNameParser = new ImageNameParser(srcStr);
                    imageList.add(imageNameParser);
                    image.attr("src", imageNameParser.getGcsPath());
                } else if(srcStr.startsWith("https://storage.googleapis.com/imgtest_bucket")) {
                    existList.add(srcStr);
                }
            }
        }

        return "";
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
