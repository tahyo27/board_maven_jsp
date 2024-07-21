package com.duck.myboard.service;

import com.duck.myboard.common.GoogleImgUploadUtil;
import com.duck.myboard.domain.Board;
import com.duck.myboard.domain.Image;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.repository.ImagesRepository;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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


    public int write(BoardRequest boardRequest) {
        List<Image> imagesList = new ArrayList<>();
        MultipartFile[] images = boardRequest.getImages();
        //board id 가져오기

        Board board = BoardRequest.createConvert(boardRequest);
        int result = boardRepository.save(board);
        Long boardId = board.getId();
        log.info(">>>>>>>>>>>>>>>>>>>>> write boardId : {}", boardId);
        
        //이미지 처리
        if(images != null) {
            for (MultipartFile image : images) {
                try {
                    Map<String, String> map = googleImgUploadUtil.imgUpload(image);
                    Image img = Image.builder()
                            .boardId(boardId)
                            .originName(map.get("originName"))
                            .uniqueName(map.get("uniqueName"))
                            .imagePath(map.get("imagePath"))
                            .build();

                    imagesList.add(img);
                } catch (IOException e) {
                    log.info(">>>>>>>>>>>>>>> upload IOE : {}", e.getMessage());
                }

            }
        }

        if(!imagesList.isEmpty()) {
            imagesRepository.saveAll(imagesList);
        }


        return result;
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
