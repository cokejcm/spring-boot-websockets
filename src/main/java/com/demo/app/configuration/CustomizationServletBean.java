package com.demo.app.configuration;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

import com.demo.app.util.Constants;

@Component
public class CustomizationServletBean implements EmbeddedServletContainerCustomizer {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// Specific configuration. The entry point will be <<local>>host:9090/app
		container.setContextPath(Constants.CONTEXT);
		container.setPort(Constants.PORT_NUM);
	}
}
