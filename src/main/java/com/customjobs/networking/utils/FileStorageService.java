package com.customjobs.networking.utils;

import com.customjobs.networking.configurations.FileStorageConfigurations;
import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.helpers.ScriptDbHelpers;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
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
    private ScriptDbHelpers scriptDbHelpers;

    @Autowired
    public FileStorageService(FileStorageConfigurations fileStorageConfigurations) {
        this.fileAbsolutePath = Paths.get(fileStorageConfigurations.getUploadDir());
        try {
            Files.createDirectories(this.fileAbsolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scripts storeFile(MultipartFile file, String userName) throws IOException {
        String name = getUuid();
        String filePrefix = file.getOriginalFilename().split("\\.")[0];
        String fileName = file.getOriginalFilename().replace(filePrefix, name);
        Path targetLocation = this.fileAbsolutePath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        Scripts data = scriptDbHelpers.createRecord(userName, fileName, fileAbsolutePath.toAbsolutePath().toString(), ScriptStatus.QUEUED);
        return data;
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

    private String getUuid() {
        return Generators.timeBasedGenerator().generate().toString();
    }


}
