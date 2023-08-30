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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public UserDto getMyInfo(@CurrentUser UserPrincipal userPrincipal) {
		return userService.getUserInfo(userPrincipal.getId());
	}

	@PutMapping("/me")
	public void updateMyInfo(@RequestBody @Valid UserSaveRequest userSaveRequest,
		@CurrentUser UserPrincipal userPrincipal) {
		userService.updateMyInfo(userSaveRequest, userPrincipal.getId());
	}

	@DeleteMapping("/me")
	public void deleteUser(@CurrentUser UserPrincipal userPrincipal) {
		userService.deleteUser(userPrincipal.getId());
	}

	@GetMapping("/{userId}")
	public UserDto getUserInfo(@PathVariable Long userId) {
		return userService.getUserInfo(userId);
	}

}
