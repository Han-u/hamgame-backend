package com.hamgame.hamgame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hamgame.hamgame.exception.payload.ErrorCode;
import com.hamgame.hamgame.exception.payload.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	/*
	 * @RestControllerAdvice: Exception 발생시 낚아채서 응답하는 역할을 함!
	 * @ExceptionHandler: CustomException이 발생하는 모든 익셉션을 가로챔
	 */

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		ErrorResponse response = ErrorResponse.builder()
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
		return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
	}
}
