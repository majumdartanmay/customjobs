package com.customjobs.networking.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
@Data
public class FileStorageConfigurations {
    private String uploadDir;
}
