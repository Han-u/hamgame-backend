package com.hamgame.hamgame.domain.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {
	@NotBlank
	private String nickname;
	private String bio;
	private String imageUrl;
}
