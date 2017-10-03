package com.demo.app.util;

public final class Constants {

	// Paths
	public static final String CONTROLLER_PACKAGE = "com.demo.app.controller";
	public static final String SERVICE_PACKAGE = "com.demo.app.service";
	public static final String DOMAIN_PACKAGE = "com.demo.app.domain";
	public static final String CONFIGURATION_PACKAGE = "com.demo.app.configuration";
	public static final String CONTROLLER = "Controller";
	public static final String SERVICE = "Service";

	// Token expiration time in days
	public static int EXPIRATION_DAYS = 365;
	// Token secret key
	public static final String TOKEN_KEY = "?ymsj4xmwmth*uWNtmV{C]8kg8f&kU";
	// Token header attribute
	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	// i18n
	public static final String COOKIE_LANGUAGE = "locale-cookie";
	public static final String DEFAULT_COUNTRY_CODE = "en";
	// Extra Info params in UserAuthentication
	public static final String LOCALE = "locale";
	public static final String ORGANIZATION = "organization";

	// Server info
	public static final String HOST = "localhost";
	public static final String PORT = "9090";
	public static final int PORT_NUM = 9090;
	public static final String CONTEXT = "/app";
	public static final String JERSEY_CONTEXT = "/rest/*";
	public static final String COMPLETE_CONTEXT = "/app/rest";

	// Swagger
	public static final String CONTACT = "Coke";
	public static final String CONFIG_ID = "App";
	public static final String TITLE = "App Rest Services";
	public static final String VERSION = "v1";
	public static final String[] SCHEMAS = new String[] { "http", "https" };
	public static final String SWAGGER_URL = "/swagger.json";

	// urls
	public static final String LOGIN_URL = "/login";
	// Exchanges
	public static final String EXCHANGE_ALL = "app-diffusion-exchange";
	public static final String EXCHANGE_ORG = "app-org-exchange";
	public static final String EXCHANGE_USER = "app-user-exchange";
	// Queues
	public static final String QUEUE_SUFFIX = "-queue";
	// Websockets
	public static final String TOPIC_URL = "/topic/";
	public static final String QUEUE_URL = "/queue/";
	public static final String ENDPOINT_URL = "/stomp";
	public static final int RABBIT_STOMP_PORT = 6933;

}
