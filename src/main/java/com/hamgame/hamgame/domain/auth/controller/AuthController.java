package com.hamgame.hamgame.domain.auth.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.auth.dto.RefreshDto;
import com.hamgame.hamgame.domain.auth.dto.SignInRequest;
import com.hamgame.hamgame.domain.auth.dto.SignUpRequest;
import com.hamgame.hamgame.domain.auth.dto.TokenDto;
import com.hamgame.hamgame.domain.auth.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Api(tags = "계정 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "토큰 반환", description = "oauth2 redirect 테스트용")
	@GetMapping("/token")
	public TokenDto token(@RequestParam String accessToken, @RequestParam String refreshToken) {
		return TokenDto.of(accessToken, refreshToken);
	}

	@Operation(summary = "로그인", description = "로컬 로그인을 진행합니다.")
	@PostMapping("/signin")
	public TokenDto signin(@Valid @RequestBody SignInRequest signInRequest) {
		return authService.signIn(signInRequest);
	}

	@Operation(summary = "회원 가입", description = "로컬 회원 가입을 진행합니다.")
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
		authService.signUp(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body("OK");
	}

	@Operation(summary = "토큰 갱신", description = "refreshToken으로 accessToken 및 refreshToken을 갱신합니다.")
	@PostMapping("/refresh")
	public TokenDto refresh(@RequestBody RefreshDto dto) {
		return authService.refresh(dto.getRefreshToken());
	}
}
