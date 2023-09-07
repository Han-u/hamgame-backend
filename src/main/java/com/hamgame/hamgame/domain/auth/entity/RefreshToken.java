package com.hamgame.hamgame.domain.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 30L)
//1209600L
public class RefreshToken {
	@Id
	private Long userId;

	private String refreshToken;

	@Builder
	public RefreshToken(Long userId, String refreshToken) {
		this.userId = userId;
		this.refreshToken = refreshToken;
	}
}
