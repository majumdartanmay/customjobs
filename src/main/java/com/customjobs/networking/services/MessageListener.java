package com.customjobs.networking.services;

import com.customjobs.networking.dto.ScriptExecutionModel;
import com.customjobs.networking.utils.FileStorageService;
import com.customjobs.networking.utils.queue.QueueCommunicationUtils;
import com.customjobs.networking.utils.executors.ScriptExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private QueueCommunicationUtils queueCommunicationUtils;

    @RabbitListener(queues = "${custom.jobs.queue.name}")
    public void receiveScriptExecutionRequests(final ScriptExecutionModel data) {

        log.info("Received message: {} from app1 queue.", data);

        try {

            ScriptExecutors scriptExecutors = new ScriptExecutors(
                    data.getAbsolutePathOnMemory(),
                    data.getUserName(),
                    data.getScriptId());

            scriptExecutors.setQueueCommunicationUtils(queueCommunicationUtils);

            scriptExecutors.executeScript(fileStorageService);

        } catch(Exception e) {
            log.error("Internal server error occurred in API call. Bypassing message requeue {}", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${custom.jobs.execution_queue.name}")
    public void receiveScriptExecutionStatus(final String output) {
        log.info(output);
        webSocket.convertAndSend("/topic/scriptStatus", output);
        log.info("sent message");
    }
}
