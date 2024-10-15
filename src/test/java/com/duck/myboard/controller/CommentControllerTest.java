package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.domain.Comments;
import com.duck.myboard.mapper.BoardMapper;
import com.duck.myboard.request.CommentsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @DisplayName("controller 리스트 출력")
    void controller_paging_list() throws Exception {

        //given
        Long boardId = 1668L;

        //expected
        mockMvc.perform(get("/boards/{boardId}/comments", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author", Matchers.is("test123")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("comments controller write 테스트")
    void comments_controller_write_test() throws Exception {

        //given
        Long boardId = testObj();

        CommentsRequest commentsRequest = CommentsRequest.builder()
                .author("test123")
                .content("test321")
                .parentId(null)
                .build();



        String json = objectMapper.writeValueAsString(commentsRequest);

        System.out.println(">>>>>>>>>>>>>>>>>" + json);

        //expected
        mockMvc.perform(post("/boards/{boardId}/comments", boardId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                        )
                        .andExpect(status().isOk())
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