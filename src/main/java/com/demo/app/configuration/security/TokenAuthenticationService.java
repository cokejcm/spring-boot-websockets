package com.demo.app.configuration.security;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.demo.app.service.security.UserService;
import com.demo.app.util.Constants;

@Component
public class TokenAuthenticationService {

	private final TokenHandler tokenHandler;

	public TokenAuthenticationService(String secret, UserService userService) {
		tokenHandler = new TokenHandler(secret, userService);
	}

	private Locale getLocale(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constants.COOKIE_LANGUAGE)) {
					String countryCode = cookie.getValue();
					return new Locale(countryCode);
				}
			}
		}
		return new Locale(Constants.DEFAULT_COUNTRY_CODE);
	}

	public String addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
		final User user = authentication.getDetails();
		String token = tokenHandler.createTokenForUser(user);
		response.addHeader(Constants.AUTH_HEADER_NAME, token);
		return token;
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(Constants.AUTH_HEADER_NAME);
		if (token != null) {
			final User user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				// No need to hold passwd for Security reasons.
				user.eraseCredentials();
				UserAuthentication userAuthentication = new UserAuthentication(user);
				// Set the locale from the cookie in the request
				userAuthentication.addExtraInfo(Constants.LOCALE, getLocale(request));
				return userAuthentication;
			}
		}
		return null;
	}

	public Authentication getAuthenticationForQueues(final String token) {
		if (token != null) {
			final User user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				// No need to hold passwd for Security reasons.
				user.eraseCredentials();
				return new UserAuthentication(user);
			}
		}
		return null;
	}
}
