package com.duck.myboard.request;

import com.duck.myboard.domain.Board;
import com.duck.myboard.domain.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Getter
@Setter
@ToString
public class BoardRequest {

    private String title;
    private String content;
    private String author;
    private MultipartFile[] images;

    @Builder
    public BoardRequest(String title, String content, String author, MultipartFile[] image) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.images = image;
    }

    public static Board createConvert (BoardRequest boardRequest) {
        return Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(boardRequest.getAuthor())
                .build();
    }

    public static Board editConvert (Long boardId, BoardRequest boardRequest) {
        return Board.builder()
                .id(boardId)
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .build();
    }

    public static Image imageConvert(Map<String, String> map, Long boardId) {
        return Image.builder()
                .boardId(boardId)
                .originName(map.get("originName"))
                .uniqueName(map.get("uniqueName"))
                .imagePath(map.get("imagePath"))
                .build();
    }
}
