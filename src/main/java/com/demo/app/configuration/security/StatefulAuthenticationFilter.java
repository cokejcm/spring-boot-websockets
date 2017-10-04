package com.demo.app.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class StatefulAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		System.out.println("FILTER, PATH: " + ((HttpServletRequest)request).getServletPath());
		System.out.println("FILTER, SESSION ID: " + ((HttpServletRequest)request).getSession().getId());
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null){
			System.out.println("FILTER AUTH: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
		Cookie[] c = ((HttpServletRequest)request).getCookies();
		if (c!=null) {
			for (Cookie cookie : c) {
				System.out.println(cookie.getName());
				System.out.println(cookie.getValue());
			}
		}
		System.out.println(((HttpServletRequest)request).getParameter("token"));
		filterChain.doFilter(request, response);
	}
}
