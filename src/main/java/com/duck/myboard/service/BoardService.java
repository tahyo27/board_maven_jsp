package com.duck.myboard.service;

import com.duck.myboard.common.GoogleImgUploadUtil;
import com.duck.myboard.domain.Board;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final GoogleImgUploadUtil googleImgUploadUtil;
    public List<Board> getList() {
        return boardRepository.findAll();
    }

    public List<Board> getPagingList() {
        return boardRepository.getOffsetPaging();
    }


    public int write(BoardRequest boardRequest) {
        MultipartFile file = boardRequest.getImage();
        if(file.getContentType() == null || !file.getContentType().startsWith("image")) {
            log.info(">>>>>>>>>>>>>>>>> 예외 던질 예정");
        }
        String saveName = null; //todo 들어올때 예외처리 같은거 다 생각하기 지금 테스트
        try {
            saveName = googleImgUploadUtil.imgUpload(file);
        } catch (IOException e) {
            log.info(">>>>>>>>>>>>>>> upload IOE : {}", e.getMessage());
        }

        Board board = BoardRequest.createConvert(boardRequest, saveName);

        return boardRepository.save(board);
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
