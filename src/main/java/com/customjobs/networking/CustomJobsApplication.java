package com.customjobs.networking;

import com.customjobs.networking.configurations.FileStorageConfigurations;
import com.customjobs.networking.configurations.RabbitMQConfigurations;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageConfigurations.class
})
@ComponentScan
@EnableRabbit
public class CustomJobsApplication implements RabbitListenerConfigurer {

	@Autowired
	private RabbitMQConfigurations rabbitMQConfigurations;

	public static void main(String[] args) {
		SpringApplication.run(CustomJobsApplication.class, args);
	}

	public void setRabbitMQConfigurations(RabbitMQConfigurations rabbitMQConfigurations) {
		this.rabbitMQConfigurations = rabbitMQConfigurations;
	}

	@Bean
	public RabbitMQConfigurations getRabbitMQConfigurations(){return rabbitMQConfigurations;}

	@Bean
	public Queue getCustomJobsQueue() {
		return new Queue(getRabbitMQConfigurations().getQueueName());
	}

	@Bean
	Queue getScriptExecutionQueue() {
		return new Queue(getRabbitMQConfigurations().getExecutionQueueName());
	}

	@Bean
	public TopicExchange getCustomJobsExchange(){
		return new TopicExchange(getRabbitMQConfigurations().getQueueExchange());
	}

	@Bean
	public Binding declareCustomJobsBinding() {
		return BindingBuilder.
				bind(getCustomJobsQueue()).
				to(getCustomJobsExchange()).
				with(getRabbitMQConfigurations().getRoutingKey());
	}

	@Bean
	public Binding declareExecutionQueueBinding() {
		return BindingBuilder
				.bind(getScriptExecutionQueue())
				.to(getCustomJobsExchange())
				.with(getRabbitMQConfigurations().getExecutionQueueRoutingKey());
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}


	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}


	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}


	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(consumerJackson2MessageConverter());
		return factory;
	}


	@Override
	public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}
}
