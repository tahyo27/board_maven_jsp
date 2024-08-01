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

    public boolean imgUpload(ImageNameParser imageNameParser) {
        if(imageNameParser == null) {
            throw new GcsUploadException();
        }
        //임시 저장 이미지 경로
        Path tempFilePath = Paths.get("./temp/image").resolve(imageNameParser.getTempName());
        log.info(">>>>>>>>>>>>>> 이미지 들어옴 : {}", tempFilePath);
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

    public boolean imgDelete(String path) {
        if(path == null) {
            return false;
        }
        String imgName = path.replace(GCS_URI_PREFIX + bucketName + "/", ""); //todo 유니크이름 도 가져와야할듯
        Blob blob = storage.get(bucketName, imgName);
        if (blob == null) {
            log.info(">>>>>>>>> 해당 이미지 파일 스토리지에 없음 {}", imgName);
            return false;
        }
        BlobId idWithGeneration = blob.getBlobId();
        log.info(">>>>>>>>>>>>>>>>>>>>>> idWithGeneration {}", idWithGeneration);

        boolean deleted = storage.delete(idWithGeneration);

        if (deleted) {
            log.info(bucketName + "의 Object " + path + "가 삭제되었습니다 ");
        } else {
            log.warn(bucketName + "에서"  + path + "의 삭제가 실패했습니다" );
        }

        return deleted;
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
