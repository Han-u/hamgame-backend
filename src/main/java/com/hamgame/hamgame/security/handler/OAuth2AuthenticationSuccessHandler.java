package com.hamgame.hamgame.security.handler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.hamgame.hamgame.domain.auth.entity.RefreshToken;
import com.hamgame.hamgame.domain.auth.entity.repository.RefreshTokenRepository;
import com.hamgame.hamgame.security.auth.UserPrincipal;
import com.hamgame.hamgame.security.cookie.CookieAuthorizationRequestRepository;
import com.hamgame.hamgame.security.cookie.CookieUtils;
import com.hamgame.hamgame.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${spring.jwt.authorized-redirect-uris}")
	private List<String> authorizedRedirectUris;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		String targetUrl = determineTargetUrl(request, response, authentication);

		if (response.isCommitted()) {
			return;
		}
		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		Optional<String> redirectUri = CookieUtils.getCookie(request,
				CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue);

		if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
			throw new RuntimeException("redirect URIs are not matched");
		}

		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

		String accessToken = jwtTokenProvider.createAccessToken(authentication);
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

		saveRefreshToken(authentication, refreshToken);

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("accessToken", accessToken)
			.queryParam("refreshToken", refreshToken)
			.build()
			.toUriString();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}

	private boolean isAuthorizedRedirectUri(String uri) {
		URI clientRedirectUri = URI.create(uri);
		return authorizedRedirectUris.stream().anyMatch(authorizedRedirectUri -> {
			URI authorizedURI = URI.create(authorizedRedirectUri);
			return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) &&
				authorizedURI.getPort() == clientRedirectUri.getPort();
		});
	}

	private void saveRefreshToken(Authentication authentication, String refreshToken) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		refreshTokenRepository.save(
			RefreshToken.builder()
				.userId(userPrincipal.getId())
				.refreshToken(refreshToken)
				.build()
		);

	}
}
