package com.hamgame.hamgame.domain.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "로컬 회원 가입 요청 DTO")
@Getter
public class SignUpRequest {
	@Schema(description = "이름")
	@NotBlank
	private String name;

	@Schema(description = "이메일")
	@NotBlank
	@Email
	private String email;

	@Schema(description = "비밀번호")
	@NotBlank
	private String password;

	@Schema(description = "닉네임")
	@NotBlank
	private String nickname;

	@Schema(description = "자기 소개")
	private String bio;

	@Schema(description = "프로필 이미지")
	private String imageUrl;

}
