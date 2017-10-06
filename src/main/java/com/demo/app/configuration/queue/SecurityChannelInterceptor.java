package com.demo.app.configuration.queue;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
/**
 * Used to authenticate during the subscribe phase. The authentication gets inherited from the http security
 * (see StatefulAuthenticationFilter)
 * The commented code is preserved in case token authentication is needed in the future in order to setup the
 * SecurityContext
 * @author coke
 *
 */
public class SecurityChannelInterceptor extends ChannelInterceptorAdapter implements ExecutorChannelInterceptor {

	//	@Autowired
	//	private TokenAuthenticationService tokenAuthenticationService;
	//	private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();
	//	private final SecurityContext EMPTY_CONTEXT;

	//  public SecurityChannelInterceptor() {
	//		this.EMPTY_CONTEXT = SecurityContextHolder.createEmptyContext();
	//  }

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		try {
			StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
			String url = headerAccessor.getDestination();
			if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
				//Call this.setup(message) in case you need to set up the SecurityContext. The token needs to be
				//passed as part of the header from the client: stompClient.subscribe(queue, callback, headers)...
				String queueUserName = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf("-queue"));
				if (!headerAccessor.getUser().getName().equals(queueUserName)) {
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
		super.afterSendCompletion(message, channel, sent, ex);
		//this.cleanup();
	}

	@Override
	public void afterMessageHandled(Message<?> arg0, MessageChannel arg1, org.springframework.messaging.MessageHandler arg2, Exception arg3) {
		//this.cleanup();
	}

	@Override
	public Message<?> beforeHandle(Message<?> arg0, MessageChannel arg1, org.springframework.messaging.MessageHandler arg2) {
		return arg0;
	}

	//	private void setup(Message<?> message) {
	//		SecurityContext currentContext = SecurityContextHolder.getContext();
	//		Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
	//		if (contextStack == null) {
	//			contextStack = new Stack<>();
	//			ORIGINAL_CONTEXT.set(contextStack);
	//		}
	//		contextStack.push(currentContext);
	//		SecurityContext context = SecurityContextHolder.createEmptyContext();
	//		context.setAuthentication(getAuthentication(message));
	//		SecurityContextHolder.setContext(context);
	//	}
	//
	//	private Authentication getAuthentication(Message<?> message) {
	//		return tokenAuthenticationService.getAuthenticationForQueues(getHeader(message, Constants.AUTH_HEADER_NAME));
	//	}

	//	private void cleanup() {
	//		Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
	//		if (contextStack != null && !contextStack.isEmpty()) {
	//			SecurityContext originalContext = contextStack.pop();
	//			try {
	//				if (this.EMPTY_CONTEXT.equals(originalContext)) {
	//					SecurityContextHolder.clearContext();
	//					ORIGINAL_CONTEXT.remove();
	//				} else {
	//					SecurityContextHolder.setContext(originalContext);
	//				}
	//			} catch (Throwable var4) {
	//				SecurityContextHolder.clearContext();
	//			}
	//
	//		} else {
	//			SecurityContextHolder.clearContext();
	//			ORIGINAL_CONTEXT.remove();
	//		}
	//	}

	//	@SuppressWarnings("unchecked")
	//	private String getHeader(Message<?> message, String headerName) {
	//		Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) message.getHeaders().get("nativeHeaders");
	//		if (nativeHeaders != null && !nativeHeaders.isEmpty()) {
	//			return nativeHeaders.get(headerName).get(0);
	//		}
	//		return null;
	//	}
}