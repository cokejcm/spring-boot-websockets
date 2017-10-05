package com.demo.app.configuration.queue;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.demo.app.configuration.security.TokenAuthenticationService;
import com.demo.app.util.Constants;

@Component
public class TokenSecurityChannelInterceptor extends ChannelInterceptorAdapter implements ExecutorChannelInterceptor {

	private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();

	private final SecurityContext EMPTY_CONTEXT;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	public TokenSecurityChannelInterceptor() {
		this.EMPTY_CONTEXT = SecurityContextHolder.createEmptyContext();
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		try {
			StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
			String url = headerAccessor.getDestination();
			if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
				this.setup(message);
				String queueUserName = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf("-queue"));
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null || !authentication.getPrincipal().equals(queueUserName)) {
					throw new IllegalArgumentException("No permission for this topic");
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("No permission for this topic");
		}
		return message;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		this.cleanup();
	}

	@Override
	public void afterMessageHandled(Message<?> arg0, MessageChannel arg1, org.springframework.messaging.MessageHandler arg2, Exception arg3) {
		this.cleanup();
	}

	@Override
	public Message<?> beforeHandle(Message<?> arg0, MessageChannel arg1, org.springframework.messaging.MessageHandler arg2) {
		return arg0;
	}

	private void setup(Message<?> message) {
		SecurityContext currentContext = SecurityContextHolder.getContext();
		Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
		if (contextStack == null) {
			contextStack = new Stack<>();
			ORIGINAL_CONTEXT.set(contextStack);
		}
		contextStack.push(currentContext);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(getAuthentication(message));
		SecurityContextHolder.setContext(context);
	}

	private Authentication getAuthentication(Message<?> message) {
		return tokenAuthenticationService.getAuthenticationForQueues(getHeader(message, Constants.AUTH_HEADER_NAME));
	}

	private void cleanup() {
		Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
		if (contextStack != null && !contextStack.isEmpty()) {
			SecurityContext originalContext = contextStack.pop();
			try {
				if (this.EMPTY_CONTEXT.equals(originalContext)) {
					SecurityContextHolder.clearContext();
					ORIGINAL_CONTEXT.remove();
				} else {
					SecurityContextHolder.setContext(originalContext);
				}
			} catch (Throwable var4) {
				SecurityContextHolder.clearContext();
			}

		} else {
			SecurityContextHolder.clearContext();
			ORIGINAL_CONTEXT.remove();
		}
	}

	@SuppressWarnings("unchecked")
	private String getHeader(Message<?> message, String headerName) {
		Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) message.getHeaders().get("nativeHeaders");
		if (nativeHeaders != null && !nativeHeaders.isEmpty()) {
			return nativeHeaders.get(headerName).get(0);
		}
		return null;
	}
}