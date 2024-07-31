package com.duck.myboard.service;

import com.duck.myboard.common.GoogleStorageUtil;
import com.duck.myboard.common.ImageNameParser;
import com.duck.myboard.domain.Board;
import com.duck.myboard.domain.Image;
import com.duck.myboard.exception.GcsUploadException;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.repository.ImagesRepository;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImagesRepository imagesRepository;
    private final GoogleStorageUtil googleStorageUtil;

    public List<Board> getList() {
        return boardRepository.findAll();
    }

    public List<Board> getPagingList() {
        return boardRepository.getOffsetPaging();
    }

    @Transactional
    public Long write(BoardRequest boardRequest, List<ImageNameParser> parserList) {

        Board board = BoardRequest.createConvert(boardRequest);
        boardRepository.save(board);
        Long boardId = board.getId();

        if(!parserList.isEmpty()) {
            uploadImage(parserList, boardId);
        }

        return boardId;
    }

    @Transactional
    public int edit(BoardRequest boardRequest, List<ImageNameParser> parserList, List<String> deletePath) { //todo 수정할때 이미지처리 어떤방식으로 할지 고민

        Board board = BoardRequest.editConvert(boardRequest); // 글 수정후
        Long boardId = board.getId();
        int result = boardRepository.update(board);
        log.info(">>>>>>>>>>>>>>>>>>>>>>> service deletePath {}", deletePath);

        if(!deletePath.isEmpty()) { //삭제해야할 부분 삭제
           deleteImages(deletePath, boardId);
        }
        
        if(!parserList.isEmpty()) { //업로드 해야될 부분 업로드
            uploadImage(parserList, boardId);
        }
        return result;
    }

    @Transactional
    public int delete(Long boardId) {
        int result = boardRepository.deleteById(boardId);
        imagesRepository.deleteByBoardId(boardId);
        return result;
    }

    public BoardResponse get(Long boardId) {
        Board board = boardRepository.findById(boardId);
        return BoardResponse.convert(board);
    }

    public List<String> getImagePath(Long boardId) {
        return imagesRepository.pathFindByBoardId(boardId);
    }

    private void uploadImage(List<ImageNameParser> parserList, Long boardId) {
        List<Image> imageList = new ArrayList<>();
        for (ImageNameParser imageNameParser : parserList) {
            if (googleStorageUtil.imgUpload(imageNameParser)) {
                Image image = imageNameParser.convertImage(boardId);
                imageList.add(image);
            } else {
                throw new GcsUploadException();
            }
        }
        imagesRepository.saveAll(imageList);
    }

    private void deleteImages(List<String> deletePath, Long boardId) { //todo 예외처리
        for (String str : deletePath) {
            googleStorageUtil.imgDelete(str);
        }
        imagesRepository.deleteByBoardIdAndPath(boardId, deletePath);
    }

}
