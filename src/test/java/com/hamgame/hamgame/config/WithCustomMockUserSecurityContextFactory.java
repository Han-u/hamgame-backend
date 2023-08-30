package com.hamgame.hamgame.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.hamgame.hamgame.security.auth.UserPrincipal;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
	@Override
	public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
		Long userId = annotation.id();

		UserPrincipal userPrincipal = UserPrincipal.create(userId, null);

		SecurityContext context = SecurityContextHolder.getContext();
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			userPrincipal, null, userPrincipal.getAuthorities());
		authenticationToken.setDetails(userPrincipal);
		context.setAuthentication(authenticationToken);
		return context;
	}
}
