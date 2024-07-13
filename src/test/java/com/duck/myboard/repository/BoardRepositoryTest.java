package com.duck.myboard.repository;

import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    private BoardMapper boardMapper;

    @Test
    @DisplayName("mapper 저장 테스트")
    void save() {
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        int result = boardMapper.save(board);
        Assertions.assertEquals(result, 1);
    }

    @Test
    @DisplayName("mapper 저장 테스트")
    void findAll() {
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        int result = boardMapper.save(board);
        List<Board> boards = boardMapper.findAll();

        assertThat(boards.get(0).getTitle()).isEqualTo("제목");
        assertThat(boards.get(0).getContent()).isEqualTo("내용");
        assertThat(boards.get(0).getAuthor()).isEqualTo("작성자");
    }


}