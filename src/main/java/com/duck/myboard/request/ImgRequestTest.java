package com.duck.myboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@ToString
public class ImgRequestTest {

    private MultipartFile[] images;

    @Builder
    public ImgRequestTest(MultipartFile[] image) {
        this.images = images;
    }
}
