package com.customjobs.networking.controllers;

import com.customjobs.networking.configurations.RabbitMQConfigurations;
import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.utils.FileStorageService;
import com.customjobs.networking.utils.Queue.MessageSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfigurations rabbitMQConfigurations;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> storeFile(@RequestParam("userName") String userName,
                                            @RequestParam("file") MultipartFile file) {
        try {
            Scripts data = fileStorageService.storeFile(file, userName);
            messageSender.sendMessage(
                    rabbitTemplate,
                    rabbitMQConfigurations.getQueueExchange(),
                    rabbitMQConfigurations.getRoutingKey(),
                    data);
            return new ResponseEntity<>(data, HttpStatus.OK);
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
