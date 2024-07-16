package com.duck.myboard.repository;

import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import com.duck.myboard.request.BoardCreate;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.interning.qual.InternedDistinct;
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

@Slf4j
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
    @DisplayName("mapper findAll 테스트")
    void findAll() {
        //given
        Long boardId = testObj();

        //when
        List<Board> boards = boardMapper.findAll();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", boards);

        //then
        assertThat(boards.get(0).getTitle()).isEqualTo("제목");
        assertThat(boards.get(0).getContent()).isEqualTo("내용");
        assertThat(boards.get(0).getAuthor()).isEqualTo("작성자");
    }

    @Test
    @DisplayName("mapper update 테스트")
    void update() {
        //given
        Long boardId = testObj();

        Board updateBoard = Board.builder()
                .id(boardId)
                .title("제목변경")
                .content("내용변경")
                .build();
        
        int updateResult = boardMapper.update(updateBoard);

        //when
        List<Board> boards = boardMapper.findAll();

        //then
        assertThat(boards.get(0).getTitle()).isEqualTo("제목변경");
        assertThat(boards.get(0).getContent()).isEqualTo("내용변경");

    }

    @Test
    @DisplayName("mapper delete 테스트")
    void deleteById() {
        //given
        Long boardId = testObj();

        //when
        List<Board> boards = boardMapper.findAll();
        Assertions.assertEquals(boards.size(), 1);

        int result = boardMapper.deleteById(boardId);

        //then
        Assertions.assertEquals(result, 1);

    }

    @Test
    @DisplayName("mapper paging 테스트")
    void mapper_get_offset_paging_test() {
        //given
        pagingObj();

        //when
        List<Board> boardList = boardMapper.getOffsetPaging();

        //then
        Assertions.assertEquals(10, boardList.size());
        Assertions.assertEquals("제목100", boardList.get(0).getTitle());
        Assertions.assertEquals("내용100", boardList.get(0).getContent());

    }

    private Long testObj() {
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();
        int result = boardMapper.save(board);

        return board.getId();
    }

    private void pagingObj() {
        for(int i = 1; i <= 100; i++) {
            Board board = Board.builder()
                    .title("제목" + i)
                    .content("내용" + i)
                    .author("작성자" + i)
                    .build();
            boardMapper.save(board);
        }
    }


}