package com.customjobs.networking.utils;

import com.customjobs.networking.configurations.FileStorageConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileAbsolutePath;

    @Autowired
    public FileStorageService(FileStorageConfigurations fileStorageConfigurations) {
        this.fileAbsolutePath = Paths.get(fileStorageConfigurations.getUploadDir());
        try {
            Files.createDirectories(this.fileAbsolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = this.fileAbsolutePath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return targetLocation.toAbsolutePath().toString();
    }

    public Resource loadFileAsResource(String fileName) {
        Path filePath = this.fileAbsolutePath.resolve(fileName);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists())
                return resource;
            throw new RuntimeException("File not found");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("File not found");
    }
}
