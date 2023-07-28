package com.hamgame.hamgame.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	BAD_REQUEST(400, "C001", "잘못된 요청입니다."),
	INVALID_PARAMETER(400, "C002", "잘못된 요청 데이터 입니다."),
	UNAUTHORIZED(401, "C003", "인증 토큰이 올바르지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}
