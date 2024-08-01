package com.duck.myboard.controller;

import com.duck.myboard.common.GoogleStorageUtil;
import com.duck.myboard.common.ImageNameParser;
import com.duck.myboard.domain.Board;
import com.duck.myboard.exception.GcsUploadException;
import com.duck.myboard.mapper.BoardMapper;
import com.duck.myboard.request.BoardRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Autowired
    private GoogleStorageUtil googleStorageUtil;

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
        BoardRequest boardCreate = BoardRequest.builder()
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
        BoardRequest boardEdit = BoardRequest.builder()
                .title("변경된제목입니다")
                .content("변경된내용입니다")
                .author("더미값")
                .build();

        //when
        mockMvc.perform(patch("/boards/{boardId}", boardId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "title", boardEdit.getTitle(),
                                "content", boardEdit.getContent(),
                                "author", boardEdit.getAuthor()
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

    @Test
    @DisplayName("controller 등록 공백 실패 테스트 및 예외처리")
    void controller_write_request_blank_exception_test() throws Exception {
        //given
        BoardRequest boardRequest = BoardRequest.builder()
                .title("제목")
                .content("         ")
                .author("작성자")
                .build();

        //expected
        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "title", boardRequest.getTitle(),
                                "content", boardRequest.getContent(),
                                "author", boardRequest.getAuthor()
                        ))
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("error"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("controller 수정 공백 실패 테스트 및 예외처리")
    void controller_eidt_request_blank_exception_test() throws Exception {
        //given
        BoardRequest boardRequest = BoardRequest.builder()
                .title("제목")
                .content("    ")
                .build();

        //expected
        mockMvc.perform(patch("/boards/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "title", boardRequest.getTitle(),
                                "content", boardRequest.getContent()
                        ))
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("error"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("controller board get 테스트")
    void controller_board_get_test() throws Exception {
        //given
        Long boardId = testObj();


        //expected
        mockMvc.perform(get("/boards/{boardId}", boardId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andExpect(view().name("select"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("테스트 이미지 템프 성공")
    void image_temp_test_success() throws Exception {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "dummy image content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/image/temp").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    @DisplayName("이미지 temp 업로드한 이미지가 없을때")
    void image_temp_teest_empty() throws Exception {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "",
                "image/png",
                new byte[0]
        );

        // When & Then
        mockMvc.perform(multipart("/image/temp").file(mockFile))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("파일이 업로드되지 않았습니다")));
    }

    @Test
    @DisplayName("파일 이름이 null일때")
    void image_temp_test_name_null() throws Exception {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                null,
                "image/png",
                "dummy image content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/image/temp").file(mockFile))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("파일의 이름이 잘못되었습니다")));
    }

    @Test
    @DisplayName("파일 이름이 공백일때")
    void image_temp_test_name_empty() throws Exception {
        // Given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "",
                "image/png",
                "dummy image content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/image/temp").file(mockFile))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("파일의 이름이 잘못되었습니다")));
    }

    @Test
    @DisplayName("구글 스토리지 업로드 성공 테스트")
    void gcs_upload_test() throws IOException {
        //given
        Path tempDirPath = Path.of("./temp/image");
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, 200, 200);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Dummy Image", 50, 100);
        g2d.dispose();
        String uuidName = UUID.randomUUID().toString();
        String name = uuidName + "_dummyimage.png";

        //when
        File imageFile = new File(tempDirPath.toFile(), name);
        ImageIO.write(bufferedImage, "png", imageFile);

        ImageNameParser imageNameParser = new ImageNameParser("/temp/image/" + name);
        log.info("ImageName parser>>>>>>>>> {}", imageNameParser);
        boolean result = googleStorageUtil.imgUpload(imageNameParser);

        //then
        Assertions.assertTrue(result);

    }

    @Test
    @DisplayName("구글 스토리지 업로드 null 테스트")
    void gcs_upload_fail_test() throws IOException {

        //expected
        Assertions.assertThrows(GcsUploadException.class, () -> googleStorageUtil.imgUpload(null));

    }

    @Test
    @DisplayName("구글 스토리지 업로드 삭제 테스트")
    void gcs_upload_delete_test() throws IOException {
        //given
        Path tempDirPath = Path.of("./temp/image");

        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, 200, 200);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Dummy Image", 50, 100);
        g2d.dispose();
        String uuidName = UUID.randomUUID().toString();
        String name = uuidName + "_dummyimage.png";
        File imageFile = new File(tempDirPath.toFile(), name);
        ImageIO.write(bufferedImage, "png", imageFile);

        ImageNameParser imageNameParser = new ImageNameParser("/temp/image/" + name);
        String gcsPath = imageNameParser.getGcsPath();
        googleStorageUtil.imgUpload(imageNameParser);
        //when
        boolean result = googleStorageUtil.imgDelete(gcsPath);

        //then
        Assertions.assertTrue(result);

    }

    @Test
    @DisplayName("구글 스토리지 업로드 삭제 실패 테스트")
    void gcs_upload_delete_fail_test() throws IOException {
        //given
        String gcsPath = null;
        //when
        boolean result = googleStorageUtil.imgDelete(gcsPath);

        //then
        Assertions.assertFalse(result);

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