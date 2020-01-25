package com.customjobs.networking.utils.queue;

import com.customjobs.networking.configurations.RabbitMQConfigurations;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class QueueCommunicationUtils {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfigurations rabbitMQConfigurations;

    @Autowired
    private MessageSender messageSender;

    public <T> void sendMessageForScriptExecution(T data) {
        messageSender.sendMessage(
                rabbitTemplate,
                rabbitMQConfigurations.getQueueExchange(),
                rabbitMQConfigurations.getRoutingKey(),
                data
                );
    }

    public <T> void sendMessageForScriptExecutionStatus (T data) {
        messageSender.sendMessage(
                rabbitTemplate,
                rabbitMQConfigurations.getQueueExchange(),
                rabbitMQConfigurations.getExecutionQueueRoutingKey(),
                data
        );
    }
}
