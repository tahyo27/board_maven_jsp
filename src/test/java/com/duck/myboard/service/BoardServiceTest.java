package com.duck.myboard.service;

import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import com.duck.myboard.repository.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardServiceTest {

    @Autowired
    private BoardMapper boardMapper;

    private BoardService boardService;

    @BeforeEach
    void init() {
        boardService = new BoardService(new BoardRepository(boardMapper));
    }

    @Test
    @DisplayName("board service 저장 테스트")
    void board_service_save_test() {
        //given
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        //when
        int result = boardService.write(board);

        //then
        Assertions.assertEquals(result, 1);
        
    }

}