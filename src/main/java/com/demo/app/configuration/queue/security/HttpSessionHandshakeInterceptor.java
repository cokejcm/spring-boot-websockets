package com.demo.app.configuration.queue.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.demo.app.configuration.security.TokenAuthenticationService;
import com.demo.app.configuration.security.UserAuthentication;
import com.demo.app.util.Constants;


@Configuration
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

	@Autowired
	TokenAuthenticationService tokenAuthenticationService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		// No need
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Map<String, Object> arg3) throws Exception {
		ServletServerHttpRequest r = (ServletServerHttpRequest) arg0;
		HttpServletRequest s = r.getServletRequest();
		String token = s.getParameter(Constants.AUTH_HEADER_NAME);
		UserAuthentication authentication;
		try {
			authentication = (UserAuthentication) tokenAuthenticationService.getAuthenticationForQueues(token);
			return authentication != null;
		} catch (Exception e) {
			logger.info("Websockets: Incorrect credentials in handshake");
			return false;
		}
		/*Cookie[] cookies = s.getCookies();
		if (cookies != null) {
			Optional<Cookie> optToken = Arrays.stream(cookies).filter(c -> c.getName().equals(Constants.AUTH_HEADER_NAME)).findFirst();
			token = optToken.isPresent() ? optToken.get().getValue() : null;
		}*/
	}

}
