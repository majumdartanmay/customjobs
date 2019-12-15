package com.customjobs.networking.services;

import com.customjobs.networking.entity.Scripts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = "${custom.jobs.queue.name}")
    public void receiveScriptExecutionRequests(final Scripts data) {

        log.info("Received message: {} from app1 queue.", data);
        try {
            log.info("Making REST call to the API");
            //TODO: Code to make REST call
            log.info("<< Exiting receiveMessageForApp1() after API call.");
        } catch(Exception e) {
            log.error("Internal server error occurred in API call. Bypassing message requeue {}", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
