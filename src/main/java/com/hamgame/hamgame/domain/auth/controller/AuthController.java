package com.hamgame.hamgame.domain.auth.controller;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.auth.dto.RefreshDto;
import com.hamgame.hamgame.domain.auth.dto.TokenDto;
import com.hamgame.hamgame.domain.auth.service.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {
	private final AuthService authService;

	@Value("${spring.jwt.token.secret-key}")
	private String key;

	@GetMapping("/auth/token")
	public TokenDto token(@RequestParam String accessToken, @RequestParam String refreshToken) {
		return TokenDto.of(accessToken, refreshToken);
	}

	@GetMapping("/auth/test")
	public String getTestToken() {
		// 임시 토큰 발급용
		Date now = new Date();
		Date accessValidity = new Date(now.getTime() + 1209600000);
		Claims claims = Jwts.claims().setSubject("4");
		claims.put("email", "hsy1607@gmail.com");
		byte[] keyBytes = Decoders.BASE64.decode(key);
		Key shaKey = Keys.hmacShaKeyFor(keyBytes);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(accessValidity)
			.signWith(shaKey, SignatureAlgorithm.HS256)
			.compact();
	}

	@PostMapping("/auth/refresh")
	public TokenDto refresh(@RequestBody RefreshDto dto) {
		return authService.refresh(dto.getRefreshToken());
	}
}
