package com.demo.app.configuration.queue;

import java.security.Principal;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.demo.app.configuration.security.TokenAuthenticationService;
import com.demo.app.util.Constants;

@Component
public class TokenSecurityChannelInterceptor extends ChannelInterceptorAdapter implements ExecutorChannelInterceptor {

	private static final ThreadLocal<Stack<SecurityContext>> ORIGINAL_CONTEXT = new ThreadLocal<>();

	private final SecurityContext EMPTY_CONTEXT;
	private final Authentication anonymous;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	public TokenSecurityChannelInterceptor() {
		this.EMPTY_CONTEXT = SecurityContextHolder.createEmptyContext();
		this.anonymous = new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList(new String[]{"ROLE_ANONYMOUS"}));
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
		if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
			Principal userPrincipal = headerAccessor.getUser();
			if(!validateSubscription(userPrincipal, headerAccessor.getDestination()))
			{
				throw new IllegalArgumentException("No permission for this topic");
			}
		}
		return message;
	}

	private boolean validateSubscription(Principal principal, String topicDestination)
	{
		if (principal == null) {
			// unauthenticated user
			return false;
		}
		//Additional validation logic coming here
		return true;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		this.cleanup();
	}


	@Override
	public void afterMessageHandled(Message<?> arg0, MessageChannel arg1,
			org.springframework.messaging.MessageHandler arg2, Exception arg3) {
		this.cleanup();

	}

	@Override
	public Message<?> beforeHandle(Message<?> arg0, MessageChannel arg1,
			org.springframework.messaging.MessageHandler arg2) {
		this.setup(arg0);
		return arg0;
	}

	private void setup(Message<?> message) {
		SecurityContext currentContext = SecurityContextHolder.getContext();
		Stack<SecurityContext> contextStack = ORIGINAL_CONTEXT.get();
		if (contextStack == null) {
			contextStack = new Stack<SecurityContext>();
			ORIGINAL_CONTEXT.set(contextStack);
		}

		contextStack.push(currentContext);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(getAuthentication(message));
		SecurityContextHolder.setContext(context);
	}

	// checking the custom headers passed with the message
	// and authenticated via a custom token service
	private Authentication getAuthentication(Message<?> message) {
		Authentication authentication = this.anonymous;
		@SuppressWarnings("unchecked")
		Map<String, List<String>> nativeHeaders = (Map<String, List<String>>)message.getHeaders().get("nativeHeaders");
		if (nativeHeaders != null) {
			List<String> tokenList = nativeHeaders.get(Constants.AUTH_HEADER_NAME);
			if(tokenList != null && !tokenList.isEmpty()) {
				Authentication tokenAuthentication = tokenAuthenticationService.getAuthenticationForQueues(tokenList.get(0).toString());
				if (tokenAuthentication != null) {
					authentication = tokenAuthentication;
				}
			}
		}
		return authentication;
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
}