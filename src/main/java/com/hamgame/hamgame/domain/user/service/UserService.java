package com.hamgame.hamgame.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.user.dto.UserDto;
import com.hamgame.hamgame.domain.user.dto.UserSaveRequest;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserDto getUserInfo(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return UserDto.of(user);
	}

	@Transactional
	public void updateMyInfo(UserSaveRequest userSaveRequest, Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		user.updateInfo(userSaveRequest.getNickname(), user.getBio(), userSaveRequest.getImageUrl());
	}

	@Transactional
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		userRepository.delete(user);
	}

}
