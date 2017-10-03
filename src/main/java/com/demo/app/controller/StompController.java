package com.demo.app.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@org.springframework.stereotype.Controller
public class StompController {

	@MessageMapping("/msg-stomp")
	@SendTo("/topic/greetings")
	public String getMessage(String message) {
		System.out.println("GETMESSAGE: " + message);
		return message;
	}
}
