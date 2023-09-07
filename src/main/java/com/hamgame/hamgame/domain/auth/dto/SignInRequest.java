package com.hamgame.hamgame.domain.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "로컬 로그인 요청DTO")
@Getter
public class SignInRequest {
	@Schema(description = "이메일")
	@NotBlank
	@Email
	private String email;

	@Schema(description = "비밀번호")
	@NotBlank
	private String password;
}
