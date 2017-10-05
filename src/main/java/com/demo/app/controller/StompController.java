package com.demo.app.controller;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

@org.springframework.stereotype.Controller
public class StompController {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@MessageMapping("/msg-stomp/{queue}")
	@SendTo("/queueApp/{queue}")
	public String getMessage(String message, @DestinationVariable String queue) {
		System.out.println("GETMESSAGE: " + message + " to: " + queue);
		return message;
	}

	@SubscribeMapping("/queueApp/{queue}")
	public String subscribe(@DestinationVariable String queue) {
		System.out.println("ENTRA");
		Message m = rabbitTemplate.receive(queue);
		return m != null ? new String(m.getBody()) : null;
	}
}
