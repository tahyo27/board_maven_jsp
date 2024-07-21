package com.duck.myboard.common;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public Map<String, String> imgUpload(MultipartFile imageFile) throws IOException { //업로드 이미지 주소 반환
        String originName = imageFile.getOriginalFilename();
        //UUID로 이미지 이름 중복 방지
        String uuidName = UUID.randomUUID().toString();

        //저장 이름 _ 합치기
        String imagePath = GCS_URI_PREFIX + uuidName;
        log.info(">>>>>>>>>>>>>>>>>>> uuidName : {}", uuidName);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuidName).build();

        Blob blob = storage.create(blobInfo, imageFile.getBytes());

        return Map.of("originName", originName, "uniqueName", uuidName, "imagePath", imagePath);
    }

}
