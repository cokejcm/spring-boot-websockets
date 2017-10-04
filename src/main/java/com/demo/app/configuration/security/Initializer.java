package com.demo.app.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
public class Initializer extends AbstractHttpSessionApplicationInitializer {

	public Initializer() {
		super(SessionConfig.class);
	}
}
