package com.hamgame.hamgame.domain.user.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.domain.user.dto.UserDto;
import com.hamgame.hamgame.domain.user.dto.UserSaveRequest;
import com.hamgame.hamgame.domain.user.service.UserService;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Api(tags = "사용자 정보")
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

	private final UserService userService;

	@Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
	@GetMapping("/me")
	public UserDto getMyInfo(@CurrentUser UserPrincipal userPrincipal) {
		return userService.getUserInfo(userPrincipal.getId());
	}

	@Operation(summary = "내 정보 수정", description = "내 정보를 수정합니다.")
	@PutMapping("/me")
	public void updateMyInfo(@RequestBody @Valid UserSaveRequest userSaveRequest,
		@CurrentUser UserPrincipal userPrincipal) {
		userService.updateMyInfo(userSaveRequest, userPrincipal.getId());
	}

	@Operation(summary = "회원 탈퇴", description = "계정을 비활성화 합니다.")
	@DeleteMapping("/me")
	public void deleteUser(@CurrentUser UserPrincipal userPrincipal) {
		userService.deleteUser(userPrincipal.getId());
	}

	@Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
	@GetMapping("/{userId}")
	public UserDto getUserInfo(@PathVariable Long userId) {
		return userService.getUserInfo(userId);
	}

}
