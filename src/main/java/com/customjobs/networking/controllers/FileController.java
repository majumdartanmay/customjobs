package com.customjobs.networking.controllers;

import com.customjobs.networking.configurations.RabbitMQConfigurations;
import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.helpers.ScriptDbHelpers;
import com.customjobs.networking.utils.FileStorageService;
import com.customjobs.networking.utils.queue.MessageSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("File")
public class FileController {


    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ScriptDbHelpers scriptDbHelpers;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> storeFile(
            @RequestParam("userName") String userName,
            @RequestParam("file") MultipartFile file)
    {
        try {
            Scripts data = fileStorageService.storeFile(file, userName);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userName}/scripts")
    public ResponseEntity<?> getScripts(
            @PathVariable("userName") String userName,
            @RequestParam(value = "startDate", required = false) Optional<LocalDateTime> _startDate,
            @RequestParam(value = "endDate", required = false) Optional<LocalDateTime> _endDate,
            @RequestParam(value = "offset", required = false) Optional<Integer> _offset,
            @RequestParam(value = "limit", required = false) Optional<Integer> _limit
            ) {

        LocalDateTime startDate = _startDate.orElse(LocalDateTime.MIN);
        LocalDateTime endDate = _endDate.orElse(LocalDateTime.MAX);
        int offset = _offset.orElse(0);
        int limit = _limit.orElse(10);
        var userScripts =  scriptDbHelpers.getUserScripts(userName, startDate, endDate, limit, offset);
        return new ResponseEntity<>(userScripts, HttpStatus.OK);

    }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }
}
