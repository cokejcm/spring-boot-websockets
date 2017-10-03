package com.demo.app.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.app.configuration.security.TokenAuthenticationService;
import com.demo.app.configuration.security.UserAuthentication;
import com.demo.app.service.security.UserService;
import com.demo.app.util.Constants;

@Component
public class LoginController {

	@Autowired
	UserService userService;
	@Autowired
	TokenAuthenticationService tokenAuthenticationService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void authenticate(@Context HttpServletResponse response, com.demo.app.domain.security.User userForm) {
		// Validate the credentials against the Db
		User user;
		try {
			user = userService.loadUserByUsername(userForm.getUsername());
			if (passwordEncoder.matches(userForm.getPassword(), user.getPassword())) {
				// Generate the token and send it back in the header
				UserAuthentication authentication = new UserAuthentication(user);
				tokenAuthenticationService.addAuthentication(response, authentication);
				// Add Locale cookie
				Cookie cookie = new Cookie(Constants.COOKIE_LANGUAGE, userForm.getCountryCode());
				cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
				response.addCookie(cookie);
			} else {
				throw new WebApplicationException(Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("").build());
			}
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity("").build());
		} finally {
			userForm = null;
			user = null;
		}
	}

}
