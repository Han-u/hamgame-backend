package com.hamgame.hamgame;

import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hamgame.hamgame.config.security.jwt.JwtTokenProvider;
import com.hamgame.hamgame.domain.RefreshToken;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.dto.TokenDto;
import com.hamgame.hamgame.repository.RefreshTokenRepository;
import com.hamgame.hamgame.repository.UserRepository;

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
