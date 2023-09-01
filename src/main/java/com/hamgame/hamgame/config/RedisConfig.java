package com.hamgame.hamgame.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
@Profile("prod|dev")
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	// @Value("${spring.redis.url}")
	// private String url;

	// @Value("${spring.redis.cluster.nodes}")
	// private List<String> clusterNodes;

	@Value("${spring.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
		// return new LettuceConnectionFactory(redisClusterConfiguration);
		// RedisURI redisURI = RedisURI.create(url);
		// RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
		// LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
		// factory.afterPropertiesSet();
		// return factory;
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	public CacheManager contentCacheManager(RedisConnectionFactory cf) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
			.entryTtl(Duration.ofMinutes(30));
		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf)
			.cacheDefaults(redisCacheConfiguration)
			.build();
	}
}
