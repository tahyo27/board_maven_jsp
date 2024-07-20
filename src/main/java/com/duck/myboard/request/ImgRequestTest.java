package com.duck.myboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Getter
@ToString
public class ImgRequestTest {

    private MultipartFile image;

    @Builder
    public ImgRequestTest(MultipartFile image) {
        this.image = image;
    }
}
