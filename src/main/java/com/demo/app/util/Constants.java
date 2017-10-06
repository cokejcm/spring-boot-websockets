package com.demo.app.util;

public final class Constants {

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
	public static final int PORT_NUM = 9090;
	public static final String CONTEXT = "/app";

	// Websockets
	public static final String QUEUE_URL = "/queue/";
	public static final String ENDPOINT_URL = "/stomp";
	public static final String EXCHANGE_URL = "/exchange";
	public static final int RABBIT_STOMP_PORT = 6933;

}
