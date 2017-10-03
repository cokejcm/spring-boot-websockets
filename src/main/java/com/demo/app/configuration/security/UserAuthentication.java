package com.demo.app.configuration.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.demo.app.domain.security.Authority;
import com.demo.app.domain.security.Rol;
import com.demo.app.util.Constants;

public class UserAuthentication implements Authentication {

	private static final long serialVersionUID = -4387968095531358197L;
	private final User user;
	private boolean authenticated = true;
	private Map<String, Object> extraInfo;

	public UserAuthentication(User user) {
		this.user = user;
		this.extraInfo = new HashMap<>();
	}

	public UserAuthentication(User user, Locale locale) {
		this.user = user;
		this.extraInfo = new HashMap<>();
		this.extraInfo.put(Constants.LOCALE, locale);
	}

	@Override
	public String getName() {
		return user.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return user.getPassword();
	}

	@Override
	public User getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public Object getExtraInfo(String id) {
		return this.extraInfo.get(id);
	}

	public void addExtraInfo(String id, Object object) {
		this.extraInfo.put(id, object);
	}

	public boolean isBasicUser() {
		return this.user.getAuthorities().size() == 1 && this.user.getAuthorities().contains(new Authority(Rol.BASIC_ROLE));
	}

}
