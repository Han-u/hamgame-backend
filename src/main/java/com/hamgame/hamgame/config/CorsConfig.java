package com.hamgame.hamgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/*
	 * CORS: Cross-Origin Resource Sharing
	 * 다른 도메인 주소의 자원을 공유하는 행위
	 * 자원 공유에 허용할 설정을 할 수 있다.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
			// 모든 경로에 대해
		registry.addMapping("/**")
			// 모든 Origin 허용
			.allowedOrigins("*")
			// GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
	}
}
