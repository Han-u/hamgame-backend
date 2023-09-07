package com.hamgame.hamgame.domain.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.auth.dto.RefreshDto;
import com.hamgame.hamgame.domain.auth.dto.TokenDto;
import com.hamgame.hamgame.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth/refresh")
	public TokenDto refresh(@RequestBody RefreshDto dto) {
		return authService.refresh(dto.getRefreshToken());
	}
}
