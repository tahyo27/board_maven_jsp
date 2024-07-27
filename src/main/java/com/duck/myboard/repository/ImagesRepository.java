package com.duck.myboard.repository;

import com.duck.myboard.domain.Image;
import com.duck.myboard.mapper.ImageMapper;
import com.duck.myboard.request.ImgRequestTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ImagesRepository {

    private final ImageMapper imageMapper;

    public int save(Image images) {
        return imageMapper.save(images);
    }

    public int saveAll(List<Image> images) {
        return imageMapper.saveAll(images);
    }

    public List<String> pathFindByBoardId(Long boardId) {
        return imageMapper.pathFindByBoardId(boardId);
    }

    public int deleteByBoardIdAndPath(Long boardId, List<String> deletePath) {
        return imageMapper.deleteByBoardIdAndPath(boardId, deletePath);
    }
}
