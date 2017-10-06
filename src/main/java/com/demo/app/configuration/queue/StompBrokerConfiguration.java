package com.demo.app.configuration.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.demo.app.configuration.security.TokenAuthenticationService;
import com.demo.app.util.Constants;

@Configuration
@EnableWebSocketMessageBroker
public class StompBrokerConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

	@Autowired
	TokenAuthenticationService tokenAuthenticationService;
	@Autowired
	private SecurityChannelInterceptor tokenSecurityChannelInterceptor;

	/**
	 * Handshake
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(Constants.ENDPOINT_URL)
				.setAllowedOrigins("*")
				.withSockJS();
	}

	/*
	 * Register /topic for the client to subscribe to.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes(Constants.CONTEXT);
		config.enableStompBrokerRelay(Constants.QUEUE_URL, Constants.EXCHANGE_URL)
				.setRelayHost("192.168.1.37")
				.setRelayPort(Constants.RABBIT_STOMP_PORT)
				.setSystemLogin("guest")
				.setSystemPasscode("guest")
				.setClientLogin("guest")
				.setClientPasscode("guest");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(new ChannelInterceptor[] { this.securityContextChannelInterceptor() });
	}

	@Bean
	public SecurityChannelInterceptor securityContextChannelInterceptor() {
		return tokenSecurityChannelInterceptor;
	}
}
