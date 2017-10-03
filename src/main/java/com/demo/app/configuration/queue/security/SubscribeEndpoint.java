package com.demo.app.configuration.queue.security;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SubscribeEndpoint {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@SubscribeMapping("/queue-app/{queueName}")
	public void init(@DestinationVariable("queueName") String queueName, SimpMessageHeaderAccessor accessor) {
		rabbitTemplate.setQueue("queue-test");
		rabbitTemplate.receiveAndConvert("queue-test");
	}
}
