package com.duck.myboard.common;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleStorageConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyLocation;

    @Bean
    public Storage storage() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyLocation).openStream();
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();
    }

}
