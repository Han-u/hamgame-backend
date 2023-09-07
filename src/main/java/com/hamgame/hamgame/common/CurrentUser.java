package com.hamgame.hamgame.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE})
@AuthenticationPrincipal
public @interface CurrentUser {
	/*
	 * Retention: Runtime까지 유지
	 * Target: 타겟은 파라미터에만 붙임
	 * AuthenticationPrincipal: 인증 정보가 존재하지 않으면 null 존재하면 user 반환
	 */
}
