package com.duck.myboard.common;

import com.duck.myboard.config.GoogleStorageConfig;
import com.duck.myboard.domain.Image;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Getter
@ToString
public class ImageNameParser {

    private final String originName;
    private final String uuidName;
    private final String gcsPath;
    private final String tempName;
    private final String extension;

    public ImageNameParser(String src) {
        String replacedName = src.replace("/temp/image/", "");

        String[] parts = replacedName.split("_", 2);
        String origin = parts[1];
        String fileExtension = origin != null && origin.contains(".") //확장자 추출
                ? origin.substring(origin.lastIndexOf('.')) : "";
        String unique = parts[0] + fileExtension;
        String path = "https://storage.googleapis.com/imgtest_bucket/" + unique;

        this.extension = fileExtension;
        this.tempName = replacedName;
        this.originName = origin;
        this.uuidName = unique;
        this.gcsPath = path;

    }

    public Image convertImage() {
        return Image.builder()
                .originName(originName)
                .uniqueName(uuidName)
                .imagePath(gcsPath)
                .build();
    }

}
