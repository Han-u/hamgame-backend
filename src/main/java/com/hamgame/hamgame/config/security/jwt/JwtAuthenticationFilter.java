package com.hamgame.hamgame.config.security.jwt;

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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	public static final String BEARER = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";

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
			return bearerToken.substring(BEARER.length());
		}
		return bearerToken;
	}

	private boolean isValidHeader(String bearerToken) {
		return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER);
	}
}
