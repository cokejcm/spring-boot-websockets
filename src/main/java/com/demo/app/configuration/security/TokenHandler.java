package com.demo.app.configuration.security;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.userdetails.User;

import com.demo.app.service.security.UserService;
import com.demo.app.util.Constants;
import com.google.common.base.Preconditions;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class TokenHandler {

	private final String secret;
	private final UserService userService;

	public TokenHandler(String secret, UserService userService) {
		if (secret == null || secret.trim().length() <= 0) {
			throw new IllegalArgumentException();
		}
		this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
		this.userService = Preconditions.checkNotNull(userService);
	}

	public User parseUserFromToken(String token) {
		String username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		return userService.loadUserByUsername(username);
	}

	private Date expirationDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, Constants.EXPIRATION_DAYS);
		return c.getTime();
	}

	public String createTokenForUser(User user) {
		Date now = new Date();
		return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(user.getUsername()).setIssuedAt(now).setExpiration(expirationDate()).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
