package com.hamgame.hamgame.domain.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@RestController
public class UserController {

	@GetMapping("")
	public User getUserInfo(@PathVariable int userId) {
		return null;
	}

	@PutMapping("")
	public void updateUserInfo(@PathVariable int userId) {
	}

	@DeleteMapping("")
	public void deleteUser(@PathVariable int userId) {
	}

}
