package com.duck.myboard.common;

import com.duck.myboard.exception.GcsUploadException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleStorageUtil {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private static final String GCS_URI_PREFIX = "https://storage.googleapis.com/";

    private final Storage storage;

    public boolean imgUpload(ImageNameParser imageNameParser) { //todo boardId받아서 루트 어떻게 처리할지
        //임시 저장 이미지 경로
        Path tempFilePath = Paths.get("./temp/image").resolve(imageNameParser.getTempName());

        try {
            byte[] imageFile = Files.readAllBytes(tempFilePath);

            String contentType = getContentType(imageNameParser);

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, imageNameParser.getUuidName())
                    .setContentType(contentType)
                    .build();

            Blob blob = storage.create(blobInfo, imageFile);

            return blob != null && blob.getBlobId() != null;

        } catch (IOException e) {
            throw new GcsUploadException(e);
        }
    }

    public void imgDelete(String path) {
        log.info(">>>>>>>>>>>>>> path {}", path);
        String imgName = path.replace(GCS_URI_PREFIX + bucketName + "/", ""); //todo 유니크이름 도 가져와야할듯
        log.info(">>>>>>>>>>>>>> imgName {}", imgName);
        Blob blob = storage.get(bucketName, imgName);
        if (blob == null) {
            return; //todo 파일 찾을 수 없다고 throw는 필요없을듯 처리 생각 로그만 남기는게 괜찮을듯 나중에 로그 보고 지우면 되니까
        }
        BlobId idWithGeneration = blob.getBlobId();
        log.info(">>>>>>>>>>>>>>>>>>>>>> idWithGeneration {}", idWithGeneration);

        boolean deleted = storage.delete(idWithGeneration);

        if (deleted) {
            log.info("Object " + path + " was permanently deleted from " + bucketName);
        } else {
            log.warn("Failed to delete object " + path + " from " + bucketName);
        }
    }

    private static String getContentType(ImageNameParser imageNameParser) {
        String extension = imageNameParser.getExtension(); //확장자

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if (extension.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        } else if (extension.endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF_VALUE;
        }
        return contentType;
    }

}
