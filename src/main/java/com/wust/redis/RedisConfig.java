package com.wust.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
//启用缓存
@EnableCaching
//@Getter
//@Setter
public class RedisConfig extends CachingConfigurerSupport {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean(name = "jedis.pool")
	@Autowired
	public JedisPool jedisPool(
			@Qualifier("jedis.pool.config") JedisPoolConfig config,
			@Value("${spring.redis.host}") String host,
			@Value("${spring.redis.port}") int port,
			@Value("${spring.redis.password}") String password) {
		logger.info("缓存服务器的地址：" + host + ":" + port);
		return new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT,
				null);
	}

	@Bean(name = "jedis.pool.config")
	public JedisPoolConfig jedisPoolConfig(
			@Value("${spring.redis.timeout}") int maxTotal,
			@Value("${spring.redis.pool.max-idle}") int maxIdle,
			@Value("${spring.redis.pool.max-wait}") int maxWaitMillis) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);

		logger.info("缓存服务器信息JedisPoolConfig：" + config);
		return config;
	}

}
