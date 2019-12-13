package com.customjobs.networking.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.properties")
public class RabbitMQConfigurations {

    @Value("custom.jobs.exchange.name")
    private String queueExchange;

    @Value("custom.jobs.queue.name")
    private String queueName;

    @Value("custom.jobs.routing.key")
    private String routingKey;
}
