package com.duck.myboard.repository;

import com.duck.myboard.domain.Comments;
import com.duck.myboard.mapper.CommentsMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentsRepositoryTest {

    @Autowired
    private CommentsMapper commentsMapper;


    @Test
    @DisplayName("댓글 매퍼 저장 테스트")
    void comments_mapper_save_test() {
        //given
        Comments comments = Comments.builder()
                .author("asdf")
                .content("asdfadf")
                .boardId(40L)
                .build();

        //when
        int result = commentsMapper.save(comments);

        //then
        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("댓글 매퍼 보드 아이디로 불러오기 테스트")
    void comments_mapper_findByBoardId_test() {
        //given
        Comments comments1 = Comments.builder()
                .author("asdf")
                .content("asdfadf")
                .boardId(40L)
                .build();

        Comments comments2 = Comments.builder()
                .author("asdf")
                .content("asdasdffadf")
                .boardId(40L)
                .build();

        commentsMapper.save(comments1);
        commentsMapper.save(comments2);
        //when
        List<Comments> list = commentsMapper.findByBoardId(40L);

        //then
        Assertions.assertEquals(2, list.size());
    }
}