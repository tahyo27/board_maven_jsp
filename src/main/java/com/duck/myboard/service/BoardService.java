package com.duck.myboard.service;

import com.duck.myboard.common.GoogleImgUploadUtil;
import com.duck.myboard.common.ImageNameParser;
import com.duck.myboard.domain.Board;
import com.duck.myboard.domain.Image;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.repository.ImagesRepository;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImagesRepository imagesRepository;
    private final GoogleImgUploadUtil googleImgUploadUtil;
    public List<Board> getList() {
        return boardRepository.findAll();
    }

    public List<Board> getPagingList() {
        return boardRepository.getOffsetPaging();
    }

    @Transactional
    public Long write(BoardRequest boardRequest, List<ImageNameParser> parserList) throws IOException {

        Board board = BoardRequest.createConvert(boardRequest);
        boardRepository.save(board);
        Long boardId = board.getId();
        log.info(">>>>>>>>>>>>>>>>>>>>> write boardId : {}", boardId);

        if(!parserList.isEmpty()) {
            List<Image> imageList = new ArrayList<>();
            for(int i = 0; i < parserList.size(); i++) {
                if(googleImgUploadUtil.imgUpload(parserList.get(0))) {
                    Image image = parserList.get(0).convertImage(boardId);
                    imageList.add(image);
                }
            }
            imagesRepository.saveAll(imageList);
        }
        return boardId;
    }

    public int edit(Long boardId, BoardRequest boardRequest) {

        Board board = BoardRequest.editConvert(boardId, boardRequest);

        return boardRepository.update(board);
    }

    public int delete(Long boardId) {
        return boardRepository.deleteById(boardId);
    }

    public BoardResponse get(Long boardId) {
        Board board = boardRepository.findById(boardId);
        return BoardResponse.convert(board);
    }
}
