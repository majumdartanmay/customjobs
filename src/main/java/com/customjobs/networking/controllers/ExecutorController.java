package com.customjobs.networking.controllers;

import com.customjobs.networking.configurations.RabbitMQConfigurations;
import com.customjobs.networking.dto.ScriptExecutionModel;
import com.customjobs.networking.utils.FileStorageService;
import com.customjobs.networking.utils.Queue.MessageSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("executor")
public class ExecutorController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfigurations rabbitMQConfigurations;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    FileStorageService fileStorageService;


    @PostMapping("/executeScript")
    public ResponseEntity<?> executeScript(
            @RequestParam("scriptId") String scriptId,
            @RequestParam("userName") String userName
    ) {

        try {
            Path completePath = fileStorageService.getCompletePath(userName);
            String absolutePath = completePath.toString();
            ScriptExecutionModel requestModel = new ScriptExecutionModel(absolutePath, scriptId, userName);
            messageSender.sendMessage(
                    rabbitTemplate,
                    rabbitMQConfigurations.getQueueExchange(),
                    rabbitMQConfigurations.getRoutingKey(),
                    requestModel);

            return new ResponseEntity<>("Queued", HttpStatus.OK);

        }catch (Exception ex) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
