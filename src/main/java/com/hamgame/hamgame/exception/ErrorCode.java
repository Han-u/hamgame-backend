package com.hamgame.hamgame.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	BAD_REQUEST(400, "C001", "잘못된 요청입니다."),
	INVALID_PARAMETER(400, "C002", "잘못된 요청 데이터 입니다."),
	UNAUTHORIZED(401, "C003", "인증 토큰이 올바르지 않습니다."),

	GAME_NOT_FOUND(404, "G001", "게임을 찾을 수 없습니다"),

	USER_NOT_FOUND(404, "U001", "유저를 찾을 수 없습니다."),

	BOARD_NOT_FOUND(404, "B001", "게시글을 찾을 수 없습니다"),

	COMMENT_NOT_FOUND(404, "CM001", "댓글을 찾을 수 없습니다.");

	private final int status;
	private final String code;
	private final String message;
}
