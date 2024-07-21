package com.duck.myboard.repository;

import com.duck.myboard.domain.Image;
import com.duck.myboard.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


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
}
