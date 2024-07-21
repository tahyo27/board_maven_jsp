package com.duck.myboard.repository;

import com.duck.myboard.domain.Image;
import com.duck.myboard.mapper.ImageMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ImageRepositoryTest {

    @Autowired
    private ImageMapper imageMapper;


    @Test
    @DisplayName("images mapper save 테스트")
    void image_mapper_save_test() {
        //given
        Image images = Image.builder()
                .imagePath("asd")
                .originName("asdf")
                .uniqueName("dfasdf")
                .boardId(430L)
                .build();
        //when

        int result = imageMapper.save(images);

        //then
        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("images mapper saveAll 테스트")
    void images_mapper_saveAll_test() {
        //given
        List<Image> imageList = new ArrayList<>();
        Image images = Image.builder()
                .imagePath("asd")
                .originName("asdf")
                .uniqueName("dfasdf")
                .boardId(431L)
                .build();
        imageList.add(images);
        imageList.add(images);
        imageList.add(images);

        //when
        int result = imageMapper.saveAll(imageList);

        //then
        Assertions.assertEquals(3, result);

    }




}