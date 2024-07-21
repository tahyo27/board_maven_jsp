package com.duck.myboard.mapper;

import com.duck.myboard.domain.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {
    int save(Image images);
    int saveAll(List<Image> images);
}
