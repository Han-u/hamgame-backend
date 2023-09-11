package com.hamgame.hamgame.exception.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	BAD_REQUEST(400, "C001", "잘못된 요청입니다."),
	INVALID_PARAMETER(400, "C002", "잘못된 요청 데이터 입니다."),
	UNAUTHORIZED(401, "C003", "인증 토큰이 올바르지 않습니다."),
	UNSUPPORTED_MEDIA_TYPE(415, "C004", "데이터 타입이 올바르지 않습니다"),

	GAME_NOT_FOUND(404, "G001", "게임을 찾을 수 없습니다"),

	USER_NOT_FOUND(404, "U001", "유저를 찾을 수 없습니다."),
	USER_EMAIL_DUPLICATE(409, "U002", "사용중인 이메일 입니다."),
	USER_NICKNAME_DUPLICATE(409, "U003", "사용중인 닉네임 입니다."),

	BOARD_NOT_FOUND(404, "B001", "게시글을 찾을 수 없습니다"),
	NOT_POST_AUTHOR(403, "B002", "게시글 작성자가 아닙니다."),

	COMMENT_NOT_FOUND(404, "CM001", "댓글을 찾을 수 없습니다."),
	NOT_COMMENT_AUTHOR(403, "CM002", "댓글 작성자가 아닙니다.");

	private final int status;
	private final String code;
	private final String message;
}
