package com.hamgame.hamgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
	/*
	 * Auditing: 감시하다. Spring JPA에서 자동으로 값을 넣어줄 수 있는 기능
	 * - @CreatedDate: 생성시 날짜 자동 생성
	 * - @LastModifiedDate: 수정시 날짜 자동 갱신
	 */
}
