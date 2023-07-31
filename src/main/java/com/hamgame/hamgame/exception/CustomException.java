package com.hamgame.hamgame.exception;

import com.hamgame.hamgame.exception.payload.ErrorCode;

public class CustomException extends RuntimeException {
	private ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}
}
