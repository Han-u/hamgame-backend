package com.hamgame.hamgame.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@EnableCaching
@Configuration
@Profile("prod|dev")
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	// @Value("${spring.redis.cluster.nodes}")
	// private List<String> clusterNodes;

	@Value("${spring.redis.port}")
	private int port;

	/*
	 * Redis의 String형식의 Json <=> Java Class 변환을 담당한다.
	 * ObjectMapper에서 LocalDateTime 타입 미지원으로인한 직렬화-역직렬화시 에러
	 * 날짜/시간 탕입 데이터를 처리하기 위해 JavaTimeModule을 추가해준다.
	 */
	@Bean
	public ObjectMapper objectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}

	/*
	* Redis Connection Factory Library
	* 1. Jedis - 멀티쓰레드 환경에서 쓰레드 안전 보장 X
	*          - Connection pool 사용으로 성능, 안전성 개선
	*          - Lettuce에 비해 하드웨어 자원 많이 필요
	* 		   - 비동기 불가능
	* 2. Lettuce - Netty 기반 redis client library
	* 			 - 비동기 지원으로 우수한 성능
	* 			 - TPS, 자원사용량 모두 Jedis에 비해 우수
	*/
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

	/*
	 * redis 서버에 요청보내기 위한 redisTemplate
	 * default: JdkSerializationRedisSerializer
	 * 다향한 자료구조의 Value는 GenericJackson2JsonRedisSerializer 이용하여 직렬화 & 역직렬화
	 * setEnableTransactionSupport: transaction을 허용한다.
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(ObjectMapper objectMapper){
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setEnableTransactionSupport(true);
		return redisTemplate;
	}

	/*
	 * Redis Cache 사용 위한 cache manger 등록
	 * 커스텀 설정 적용 위해 RedisCacheConfiguration 생성
	 * 생성한 configuration으로 RedisCacheManger 생성
	 * disableCachingNullValues - null 값 캐싱될 수 없도록 함. null값 시도시 에러 발생
	 * entryTtl - 캐시 TTL(Time to Live) 만료시간 설정
	 * serializeKeysWith - 캐시 Key 직렬화 & 역직렬화 설정
	 * serializeValuesWith - 캐시 Value 직렬화 & 역직렬화 설정
	 */
	@Bean
	public CacheManager contentCacheManager(RedisConnectionFactory cf, ObjectMapper objectMapper) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofMinutes(30))
			.serializeKeysWith(
				RedisSerializationContext.SerializationPair
				.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf)
			.cacheDefaults(redisCacheConfiguration)
			.build();
	}
}
