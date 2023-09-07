package com.hamgame.hamgame.domain.auth.service;

import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.auth.dto.SignInRequest;
import com.hamgame.hamgame.domain.auth.dto.SignUpRequest;
import com.hamgame.hamgame.domain.auth.dto.TokenDto;
import com.hamgame.hamgame.domain.auth.entity.RefreshToken;
import com.hamgame.hamgame.domain.auth.entity.repository.RefreshTokenRepository;
import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;
import com.hamgame.hamgame.security.auth.UserPrincipal;
import com.hamgame.hamgame.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public TokenDto signIn(SignInRequest signInRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				signInRequest.getEmail(),
				signInRequest.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String accessToken = jwtTokenProvider.createAccessToken(authentication);
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

		refreshTokenRepository.save(
			RefreshToken.builder()
				.userId(((UserPrincipal)authentication.getPrincipal()).getId())
				.refreshToken(refreshToken)
				.build()
		);

		return TokenDto.of(accessToken, refreshToken);
	}

	public void signUp(SignUpRequest signUpRequest) {
		if (userRepository.existsByEmailAndProvider(signUpRequest.getEmail(), Provider.LOCAL)) {
			throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATE);
		}
		if (userRepository.existsByNickname(signUpRequest.getNickname())) {
			throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
		}
		User user = User.builder()
			.name(signUpRequest.getName())
			.email(signUpRequest.getEmail())
			.password(passwordEncoder.encode(signUpRequest.getPassword()))
			.bio(signUpRequest.getBio())
			.provider(Provider.LOCAL)
			.nickname(signUpRequest.getNickname())
			.imageUrl(signUpRequest.getImageUrl())
			.build();
		userRepository.save(user);
	}

	@Transactional
	public TokenDto refresh(String refreshToken) {
		if (!isValidToken(refreshToken)) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}
		Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

		String accessToken = jwtTokenProvider.createAccessToken(authentication);
		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

		refreshTokenRepository.save(
			RefreshToken.builder()
				.userId(((UserPrincipal)authentication.getPrincipal()).getId())
				.refreshToken(refreshToken)
				.build()
		);
		return TokenDto.of(accessToken, newRefreshToken);
	}

	@Transactional(readOnly = true)
	public boolean isValidToken(String refreshToken) {
		jwtTokenProvider.validateToken(refreshToken);
		RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
		Long jwtId = jwtTokenProvider.getUserIdFromToken(refreshToken);
		return Objects.equals(tokenEntity.getUserId(), jwtId);
	}

}
