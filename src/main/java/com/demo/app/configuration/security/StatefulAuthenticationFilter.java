package com.demo.app.configuration.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.demo.app.util.Constants;

@Component
public class StatefulAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if (((HttpServletRequest)request).getCookies() == null){
			((HttpServletResponse)response).setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		//Filter is not managed by Spring, beans need to be loaded manually
		if (tokenAuthenticationService == null){
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			tokenAuthenticationService = webApplicationContext.getBean(TokenAuthenticationService.class);
		}
		Optional<Cookie> optToken = Arrays.stream(((HttpServletRequest)request).getCookies()).filter(c -> c.getName().equals(Constants.AUTH_HEADER_NAME)).findFirst();
		try {
			if (optToken.isPresent()){
				String token = optToken.get().getValue();
				UserAuthentication authentication = (UserAuthentication) tokenAuthenticationService.getAuthenticationForQueues(token);
				//SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			else {
				((HttpServletResponse)response).setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		} catch (Exception e) {
			((HttpServletResponse)response).setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		System.out.println(((HttpServletRequest)request).getServletPath());
		filterChain.doFilter(request, response);
	}
}
