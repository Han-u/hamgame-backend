package com.hamgame.hamgame.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamgame.hamgame.domain.user.dto.UserDto;
import com.hamgame.hamgame.domain.user.dto.UserSaveRequest;
import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.domain.user.service.UserService;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	Long myId = 1L;
	Long userId = 2L;

	UserSaveRequest getRequest(String nickname) {
		return UserSaveRequest.builder()
			.nickname(nickname)
			.bio("new bio")
			.imageUrl("new imageUrl")
			.build();
	}

	User getUser(Long id) {
		return User.builder()
			.id(id)
			.name("name" + id)
			.email("email" + id)
			.nickname("nickname" + id)
			.bio("bio" + id)
			.imageUrl("imageUrl" + id)
			.provider(Provider.KAKAO)
			.build();
	}

	@Test
	@DisplayName("유저 정보 조회 - 성공")
	void getUserInfo() {
		// Given
		User user = getUser(myId);
		when(userRepository.findById(myId)).thenReturn(Optional.of(user));

		// When
		UserDto actual = userService.getUserInfo(myId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		assertThat(actual).usingRecursiveComparison().isEqualTo(UserDto.of(user));
	}

	@Test
	@DisplayName("유저 정보 조회 실패 - 유저 없음")
	void getUserInfoNoUser() {
		// Given
		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When
		assertThatThrownBy(() -> userService.getUserInfo(myId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("유저 정보 수정 - 성공")
	void updateMyInfo() {
		// Given
		User user = getUser(myId);
		UserSaveRequest request = getRequest("new nickname");
		when(userRepository.findById(myId)).thenReturn(Optional.of(user));

		// When
		userService.updateMyInfo(request, myId);
		user.updateInfo(request.getNickname(), request.getBio(), request.getImageUrl());

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		assertThat(user.getNickname()).isEqualTo("new nickname");
		assertThat(user.getBio()).isEqualTo("new bio");
		assertThat(user.getImageUrl()).isEqualTo("new imageUrl");
	}

	@Test
	@DisplayName("유저 정보 수정 실패 - 유저 없음")
	void updateMyInfoNoUser() {
		// Given
		UserSaveRequest request = getRequest("new nickname");
		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When
		assertThatThrownBy(() -> userService.updateMyInfo(request, myId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("유저 정보 삭제 - 성공")
	void deleteUser() {
		// Given
		User user = getUser(myId);
		when(userRepository.findById(myId)).thenReturn(Optional.of(user));

		// When
		userService.deleteUser(myId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		verify(userRepository, times(1)).delete(user);
	}

	@Test
	@DisplayName("유저 정보 삭제 실패 - 유저 없음")
	void deleteUserNoUser() {
		// Given
		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When
		assertThatThrownBy(() -> userService.deleteUser(myId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
	}
}