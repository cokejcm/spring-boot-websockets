package com.demo.app.configuration.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.security.core.Authentication;

import com.demo.app.domain.security.Authority;
import com.demo.app.domain.security.Rol;

public class UserRoleRequestWrapper extends HttpServletRequestWrapper {

	private Authentication authentication;
	private HttpServletRequest originalRequest;

	public UserRoleRequestWrapper(Authentication authentication, HttpServletRequest originalRequest) {
		super(originalRequest);
		this.originalRequest = originalRequest;
		this.authentication = authentication;
	}

	@Override
	public boolean isUserInRole(String role) {
		return this.authentication.getAuthorities().contains(new Authority(Rol.valueOf(role)));
	}

	public static class Principal implements java.security.Principal {
		private String name;

		public Principal(String name) {
			super();
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	@Override
	public Principal getUserPrincipal() {
		return new Principal((String)this.authentication.getPrincipal());
	}

	public HttpServletRequest getOriginalRequest() {
		return originalRequest;
	}
}
