package com.demo.app.configuration.queue;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.app.util.Constants;

@Configuration
public class QueueConfiguration {

	// Exchange for everyone
	@Bean
	FanoutExchange exchangeAll() {
		return new FanoutExchange(Constants.EXCHANGE_ALL);
	}

	// Exchange to route messages to a whole organization
	@Bean
	DirectExchange exchangeOrg() {
		return new DirectExchange(Constants.EXCHANGE_ORG);
	}

	// Exchange to route messages to specific users
	@Bean
	DirectExchange userExchange() {
		return new DirectExchange(Constants.EXCHANGE_USER);
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin admin = new RabbitAdmin(connectionFactory);
		admin.declareExchange(exchangeOrg());
		admin.declareExchange(exchangeAll());
		admin.declareExchange(userExchange());
		return admin;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}
}
