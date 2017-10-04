package com.demo.app.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
public class SessionConfig {

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

		// Defaults
		redisConnectionFactory.setHostName("127.0.0.1");
		redisConnectionFactory.setPort(6379);
		return redisConnectionFactory;
	}

	/*@Bean
	public LettuceConnectionFactory connectionFactory() {
		LettuceConnectionFactory lettuceConnFactory = new LettuceConnectionFactory();
		lettuceConnFactory.setPort(6379);
		lettuceConnFactory.setHostName("127.0.0.1");
		lettuceConnFactory.afterPropertiesSet();
		return lettuceConnFactory;
	}*/

	/*@Bean
	public RedisConnectionFactory lettuceConnectionFactory() {
		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master("mymaster")
				.sentinel("127.0.0.1", 6379);
		return new LettuceConnectionFactory(sentinelConfig);
	}*/
}
