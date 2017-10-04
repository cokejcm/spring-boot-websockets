package com.demo.app.configuration.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.demo.app.configuration.security.HttpSessionIdHandshakeInterceptor;
import com.demo.app.configuration.security.TokenAuthenticationService;
import com.demo.app.util.Constants;

@Configuration
@EnableWebSocketMessageBroker
public class StompBrokerConfiguration extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

	@Autowired
	TokenAuthenticationService tokenAuthenticationService;

	/**
	 * Handshake
	 */
	@Override
	public void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(Constants.ENDPOINT_URL)
		.setAllowedOrigins("*")
		.withSockJS()
		.setInterceptors(new HttpSessionIdHandshakeInterceptor());
	}

	/*
	 * Register /topic for the client to subscribe to.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes(Constants.CONTEXT);
		config.enableStompBrokerRelay(Constants.QUEUE_URL)
		.setRelayHost("192.168.1.37")
		.setRelayPort(Constants.RABBIT_STOMP_PORT)
		.setSystemLogin("guest")
		.setSystemPasscode("guest")
		.setClientLogin("guest")
		.setClientPasscode("guest");
	}
}
