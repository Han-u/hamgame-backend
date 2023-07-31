package com.hamgame.hamgame.domain.auth.service;

import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hamgame.hamgame.domain.auth.dto.TokenDto;
import com.hamgame.hamgame.domain.auth.entity.RefreshToken;
import com.hamgame.hamgame.domain.auth.entity.repository.RefreshTokenRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	public TokenDto refresh(String refreshToken) {
		if (!isValidToken(refreshToken)) {
			throw new RuntimeException();
		}
		RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(RuntimeException::new);
		User user = userRepository.findById(tokenEntity.getUserId()).orElseThrow(RuntimeException::new);
		Authentication authentication = jwtTokenProvider.getAuthenticationByUser(user);
		String accessToken = jwtTokenProvider.createAccessToken(authentication);
		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
		tokenEntity.updateRefreshToken(newRefreshToken);
		refreshTokenRepository.save(tokenEntity);
		return TokenDto.of(accessToken, newRefreshToken);
	}

	public boolean isValidToken(String refreshToken) {
		jwtTokenProvider.validateToken(refreshToken);
		RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(RuntimeException::new);
		Long jwtId = jwtTokenProvider.getUserIdFromToken(refreshToken);
		return Objects.equals(tokenEntity.getUserId(), jwtId);
	}
}
