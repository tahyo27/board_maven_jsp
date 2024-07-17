package com.duck.myboard.service;

import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.request.BoardCreate;
import com.duck.myboard.request.BoardEdit;
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

import java.util.List;


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
        BoardCreate board = BoardCreate.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        //when
        int result = boardService.write(board);

        //then
        Assertions.assertEquals(result, 1);
    }
    @Test
    @DisplayName("board service getPagingList 테스트")
    void board_service_getList_test() {

        //when
        List<Board> boards = boardService.getPagingList();


        //then
        Assertions.assertEquals(boards.size(), 10);
    }

    @Test
    @DisplayName("board service edit 테스트")
    void board_service_edit_test() {
        //given
        Long boardId = testObj();

        BoardEdit boardEdit = BoardEdit.builder()
                .id(boardId)
                .title("변경제목")
                .content("변경내용")
                .build();

        int updateResult = boardService.edit(boardEdit);

        //when
        List<Board> boards = boardService.getPagingList();

        //then
        Assertions.assertEquals(boardEdit.getTitle(), boards.get(0).getTitle());
        Assertions.assertEquals(boardEdit.getContent(), boards.get(0).getContent());
    }

    @Test
    @DisplayName("board service delete 테스트")
    void board_service_delete_test() {
        //given
        Long boardId = testObj();

        //when
        int deleteResult = boardService.delete(boardId);

        //then
        Assertions.assertEquals(1, deleteResult);

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

}