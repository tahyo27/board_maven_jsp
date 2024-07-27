package com.duck.myboard.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ImageResponse {

    private String gcsPath;

    @Builder
    public ImageResponse(String gcsPath) {
        this.gcsPath = gcsPath;
    }

}
