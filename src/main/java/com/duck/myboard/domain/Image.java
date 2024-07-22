package com.duck.myboard.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Image {

    private Long id;
    private Long boardId;
    private String originName;
    private String uniqueName;
    private String imagePath;


    @Builder
    public Image(Long id, Long boardId, String originName, String uniqueName, String imagePath) {
        this.id = id;
        this.boardId = boardId;
        this.originName = originName;
        this.uniqueName = uniqueName;
        this.imagePath = imagePath;
    }

}
