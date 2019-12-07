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
public class FileController {
    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> storeFile(@RequestParam("userName") String userName,
                                            @RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file, userName);
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
