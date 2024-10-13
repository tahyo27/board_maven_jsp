package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardMapper boardMapper;

    @Test
    @DisplayName("controller 페이징 리스트 출력")
    void controller_paging_list() throws Exception {
        Long boardId = testObj();

        //expected
        mockMvc.perform(get("/boards/{boardId}/comments", boardId)
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boardList"))
                .andExpect(view().name("index"))
                .andDo(MockMvcResultHandlers.print());
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