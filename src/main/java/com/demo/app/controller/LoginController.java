package com.demo.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.app.configuration.security.TokenAuthenticationService;

@Controller
public class LoginController {

	@Autowired
	TokenAuthenticationService tokenAuthenticationService;

	@PostMapping("/user/login")
	public ResponseEntity<String> authenticate(@Context HttpServletRequest request) {
		try {
			Authentication authentication = tokenAuthenticationService.getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return new ResponseEntity<>("AUTHENTICATED", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("AUTHENTICATION ERROR", HttpStatus.BAD_REQUEST);
	}

}
