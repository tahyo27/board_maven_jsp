package com.duck.myboard.service;

import com.duck.myboard.common.GoogleStorageUtil;
import com.duck.myboard.common.ImageNameParser;
import com.duck.myboard.domain.Board;
import com.duck.myboard.domain.Image;
import com.duck.myboard.exception.GcsUploadException;
import com.duck.myboard.repository.ImagesRepository;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.response.BoardResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardServiceTest {
    @Autowired
    private BoardService boardService;

    @MockBean
    private GoogleStorageUtil googleStorageUtil;

    @Autowired
    private ImagesRepository imagesRepository;

    @Test
    @Transactional
    @DisplayName("board service 저장 테스트 성공")
    void board_service_save_success() {
        //given
        BoardRequest board = BoardRequest.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        List<ImageNameParser> imageNameParserList = new ArrayList<>();
        ImageNameParser imageNameParser1 = new ImageNameParser("/temp/image/asdfasdfaaas_fdsaf.png");
        ImageNameParser imageNameParser2 = new ImageNameParser("/temp/image/asdfasdfssss_fdsaf.jpg");
        imageNameParserList.add(imageNameParser1);
        imageNameParserList.add(imageNameParser2);

        when(googleStorageUtil.imgUpload(any(ImageNameParser.class))).thenReturn(true);

        //when
        Long boardId = boardService.write(board, imageNameParserList);

        //then
        Assertions.assertTrue(boardId > 0);
    }


    @Test
    @Transactional
    @DisplayName("board service 저장 테스트 스토리지 업로드 없을때")
    void board_service_save_test() {
        //given
        BoardRequest board = BoardRequest.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        List<ImageNameParser> imageNameParserList = new ArrayList<>();
        ImageNameParser imageNameParser1 = new ImageNameParser("/temp/image/asdfasdfaaas_fdsaf.png");
        ImageNameParser imageNameParser2 = new ImageNameParser("/temp/image/asdfasdfssss_fdsaf.jpg");
        imageNameParserList.add(imageNameParser1);
        imageNameParserList.add(imageNameParser2);

        //expected
        Assertions.assertThrows(GcsUploadException.class, () -> boardService.write(board, imageNameParserList));
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
    @Transactional
    @DisplayName("board service edit 테스트")
    void board_service_edit_test() {
        //given
        Long boardId = testObj();

        BoardRequest boardEdit = BoardRequest.builder()
                .title("변경제목")
                .content("변경내용")
                .build();

        int updateResult = boardService.edit(boardId, boardEdit);

        //when
        List<Board> boards = boardService.getPagingList();

        //then
        Assertions.assertEquals(boardEdit.getTitle(), boards.get(0).getTitle());
        Assertions.assertEquals(boardEdit.getContent(), boards.get(0).getContent());
    }

    @Test
    @Transactional
    @DisplayName("board service delete 테스트")
    void board_service_delete_test() {
        //given
        Long boardId = testObj();

        //when
        int deleteResult = boardService.delete(boardId);

        //then
        Assertions.assertEquals(1, deleteResult);

    }

    @Test
    @Transactional
    @DisplayName("board service findById 테스트")
    void board_service_findById_test() {
        //given
        Long boardId = testObj();

        //when
        BoardResponse boardResponse = boardService.get(boardId);

        //then
        Assertions.assertEquals("제목", boardResponse.getTitle());
        Assertions.assertEquals("내용", boardResponse.getContent());
        Assertions.assertEquals("작성자", boardResponse.getAuthor());

    }
    @Test
    @Transactional
    @DisplayName("board service 이미지포함 저장 테스트")
    void board_service_img_save_test() {
        //given
        Long boardId = testObj();

        List<Image> imageList = IntStream.range(0, 5).mapToObj(
                i -> Image.builder()
                            .boardId(boardId)
                            .originName("origin" + i)
                            .uniqueName("uniques" + i)
                            .imagePath("path" + i)
                            .build()
        ).toList();

        //when
        int result = imagesRepository.saveAll(imageList);

        //then
        Assertions.assertEquals(5, result);

    }



    private Long testObj() {
        BoardRequest boardRequest = BoardRequest.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();
        List<ImageNameParser> imageNameParserList = new ArrayList<>();

        Long result = boardService.write(boardRequest, imageNameParserList);

        return result;
    }

}