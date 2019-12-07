package com.customjobs.networking.controllers;

import com.customjobs.networking.utils.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("File")
public class FileControllers {
    @Autowired
    FileStorageService fIleStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> storeFile(@RequestParam("user") String userName,
                                            @RequestParam("file") MultipartFile file) {
        try {
            String fileName = fIleStorageService.storeFile(file);
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }
}
