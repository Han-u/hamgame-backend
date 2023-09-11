package com.hamgame.hamgame.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> object = new HashMap<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			object.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(SQLIntegrityConstraintViolationException e) {
		ErrorCode errorCode = ErrorCode.BAD_REQUEST;
		ErrorResponse response = ErrorResponse.builder()
			.code(errorCode.getCode())
			.message("상세 메시지: " + e.getMessage())
			.build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
		Map<String, String> object = new HashMap<>();
		for (ConstraintViolation<?> c : e.getConstraintViolations()) {
			object.put(String.valueOf(c.getPropertyPath()), c.getMessage());
		}
		return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
		ErrorCode errorCode = ErrorCode.UNSUPPORTED_MEDIA_TYPE;
		ErrorResponse response = ErrorResponse.builder()
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
		return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
	}
}
