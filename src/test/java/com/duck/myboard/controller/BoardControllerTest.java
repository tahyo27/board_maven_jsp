package com.duck.myboard.controller;

import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import com.duck.myboard.request.BoardCreate;
import com.duck.myboard.request.BoardEdit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardMapper boardMapper;

    @Test
    @DisplayName("controller 페이징 리스트 출력")
    void controller_paging_list() throws Exception {

        //expected
        mockMvc.perform(get("/boards")
                    )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("boardList"))
                .andExpect(view().name("index"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("controller write 테스트")
    void controller_write_test() throws Exception {

        //given
        BoardCreate boardCreate = BoardCreate.builder()
                .title("새로운제목입니다2")
                .content("새로운내용입니다2")
                .author("새로운작성자입니다2")
                .build();

        //when
        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "title", boardCreate.getTitle(),
                                "content", boardCreate.getContent(),
                                "author", boardCreate.getAuthor()
                        ))
                )
                .andExpect(status().isFound())
                .andDo(MockMvcResultHandlers.print());

        List<Board> boardList =  boardMapper.getOffsetPaging();

        //then
        Assertions.assertEquals(boardCreate.getTitle(), boardList.get(0).getTitle());
        Assertions.assertEquals(boardCreate.getContent(), boardList.get(0).getContent());
        Assertions.assertEquals(boardCreate.getAuthor(), boardList.get(0).getAuthor());

    }

    @Test
    @Transactional
    @DisplayName("controller edit 테스트")
    void controller_edit_test() throws Exception {

        //given
        Long boardId = testObj();
        BoardEdit boardEdit = BoardEdit.builder()
                .id(boardId)
                .title("변경된제목입니다")
                .content("변경된내용입니다")
                .build();

        //when
        mockMvc.perform(patch("/boards")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "id", String.valueOf(boardId),
                                "title", boardEdit.getTitle(),
                                "content", boardEdit.getContent()
                        ))
                )
                .andExpect(status().isFound())
                .andDo(MockMvcResultHandlers.print());

        List<Board> boardList =  boardMapper.getOffsetPaging();

        //then
        log.info(">>>>>>>>>>>>>>>>>> boardList = {}", boardList);
        Assertions.assertEquals(boardEdit.getTitle(), boardList.get(0).getTitle());
        Assertions.assertEquals(boardEdit.getContent(), boardList.get(0).getContent());

    }

    @Test
    @Transactional
    @DisplayName("controller delete 테스트")
    void controller_delete_test() throws Exception {
        //given
        Long boardId = testObj();
        Long before = boardMapper.getOffsetPaging().get(0).getId();

        //when

        mockMvc.perform(delete("/boards/{boardId}", boardId))
                .andExpect(status().isFound())
                .andDo(MockMvcResultHandlers.print());

        Long after = boardMapper.getOffsetPaging().get(0).getId();

        //then
        Assertions.assertNotEquals(before, after);

    }




    private String buildUrlEncodedFormEntity(String... params) { //form용 body에 넣는 메서드
        if( (params.length % 2) > 0 ) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            if( i > 0 ) {
                result.append('&');
            }
            result.
                    append(URLEncoder.encode(params[i], StandardCharsets.UTF_8)).
                    append('=').
                    append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8));
        }
        return result.toString();
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