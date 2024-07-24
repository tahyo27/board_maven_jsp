package com.duck.myboard.common;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleImgUploadUtil {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private static final String GCS_URI_PREFIX = "https://storage.googleapis.com/";

    private final Storage storage;

    public boolean imgUpload(ImageNameParser imageNameParser) throws IOException { //todo 예외처리 바꿔야함
        //임시 저장 이미지 경로
        Path tempFilePath = Paths.get("./temp/image").resolve(imageNameParser.getTempName());

        byte[] imageFile = Files.readAllBytes(tempFilePath);
        
        String extension = imageNameParser.getExtension(); //확장자
        
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if (extension.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        } else if (extension.endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF_VALUE;
        }
        
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, imageNameParser.getUuidName())
                .setContentType(contentType)
                .build();

        Blob blob = storage.create(blobInfo, imageFile);

        return blob != null && blob.getBlobId() != null;

    }

}
