package com.hamgame.hamgame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
	private String accessToken;
	private String refreshToken;

	public static TokenDto of(String accessToken, String refreshToken) {
		return TokenDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
