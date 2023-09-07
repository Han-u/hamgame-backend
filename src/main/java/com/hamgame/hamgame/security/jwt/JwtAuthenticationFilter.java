package com.hamgame.hamgame.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	public static final String BEARER = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";

	/*
	 * OncePerRequestFilter: Request 이전에 1회만 실행되는 필터
	 * 토큰 검증을 성공하면 SecurityContext에 Authentication 저장하고 다음 필터 체인을 실행한다
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String jwt = getJwtFromRequest(request);

		if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
			UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION);
		if (isValidHeader(bearerToken)) {
			log.info("bearerToken = {}", bearerToken.substring(BEARER.length()));
			return bearerToken.substring(BEARER.length());
		}
		return bearerToken;
	}

	private boolean isValidHeader(String bearerToken) {
		return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER);
	}
}
