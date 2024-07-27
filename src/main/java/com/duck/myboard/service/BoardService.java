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
        return boardId;
    }

    public int edit(Long boardId, BoardRequest boardRequest) { //todo 수정할때 이미지처리 어떤방식으로 할지 고민

        Board board = BoardRequest.editConvert(boardRequest);

        return boardRepository.update(board);
    }

    public int delete(Long boardId) {
        return boardRepository.deleteById(boardId);
    }

    public BoardResponse get(Long boardId) {
        Board board = boardRepository.findById(boardId);
        return BoardResponse.convert(board);
    }

    public List<String> getImagePath(Long boardId) {
        return imagesRepository.pathFindByBoardId(boardId);
    }

    @Transactional
    public int edittest(BoardRequest boardRequest, List<ImageNameParser> parserList, List<String> deletePath) {
        log.info(">>>>>>>>>>>>>>>>>> service board Reqeust {} ", boardRequest);
        Board board = BoardRequest.editConvert(boardRequest); // 글 수정후
        Long boardId = board.getId();
        int result = boardRepository.update(board);
        log.info(">>>>>>>>>>>>>>>>>>>>>>> service deletePath {}", deletePath);
        if(!deletePath.isEmpty()) { //클라우드 삭제
            for(String str : deletePath) {
                log.info("str >>>>>>>>>>>>> {}", str);
                googleStorageUtil.imgDelete(str);
            }
            int deleteResult = imagesRepository.deleteByBoardIdAndPath(boardId, deletePath);
        }

        if(!parserList.isEmpty()) {
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
        return result;
    }

}
