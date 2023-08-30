package com.hamgame.hamgame.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamgame.hamgame.config.TestSecurityConfig;
import com.hamgame.hamgame.config.WithCustomMockUser;
import com.hamgame.hamgame.domain.user.controller.UserController;
import com.hamgame.hamgame.domain.user.dto.UserDto;
import com.hamgame.hamgame.domain.user.dto.UserSaveRequest;
import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.service.UserService;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(value = UserController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)

	})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	ObjectMapper objectMapper = new ObjectMapper();

	Long myId = 1L;
	Long userId = 2L;

	UserSaveRequest getRequest(String nickname) {
		return UserSaveRequest.builder()
			.nickname(nickname)
			.bio("bio")
			.imageUrl("imageUrl")
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
	@DisplayName("내 정보 조회 - 성공")
	@WithCustomMockUser
	void getMyInfo() throws Exception {
		// Given
		User user = getUser(myId);
		when(userService.getUserInfo(myId)).thenReturn(UserDto.of(user));

		// When & Then
		mockMvc.perform(get("/users/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.email").value("email1"))
			.andExpect(jsonPath("$.nickname").value("nickname1"))
			.andExpect(jsonPath("$.bio").value("bio1"))
			.andExpect(jsonPath("$.imageUrl").value("imageUrl1"));

		verify(userService, times(1)).getUserInfo(anyLong());
	}

	@Test
	@DisplayName("내 정보 수정 - 성공")
	@WithCustomMockUser
	void updateMyInfo() throws Exception {
		// Given
		UserSaveRequest request = getRequest("nickname4");
		doNothing().when(userService).updateMyInfo(request, myId);

		// When & Then
		mockMvc.perform(put("/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
		verify(userService, times(1)).updateMyInfo(any(UserSaveRequest.class), anyLong());
	}

	@Test
	@DisplayName("내 정보 수정 실패 - 닉네임 없음")
	@WithCustomMockUser
	void updateMyInfoNoNickname() throws Exception {
		// Given
		UserSaveRequest request = getRequest("");

		// When & Then
		mockMvc.perform(put("/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		verifyNoInteractions(userService);
	}

	@Test
	@DisplayName("내 계정 삭제 - 성공")
	@WithCustomMockUser
	void deleteUser() throws Exception {
		// Given
		doNothing().when(userService).deleteUser(myId);

		// When & Then
		mockMvc.perform(delete("/users/me"))
			.andExpect(status().isOk());
		verify(userService, times(1)).deleteUser(anyLong());
	}

	@Test
	@DisplayName("유저 정보 조회 - 성공")
	@WithCustomMockUser
	void getUserInfo() throws Exception {
		// Given
		User user = getUser(userId);
		when(userService.getUserInfo(userId)).thenReturn(UserDto.of(user));

		// When & Then
		mockMvc.perform(get("/users/{userId}", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(2L))
			.andExpect(jsonPath("$.email").value("email2"))
			.andExpect(jsonPath("$.nickname").value("nickname2"))
			.andExpect(jsonPath("$.bio").value("bio2"))
			.andExpect(jsonPath("$.imageUrl").value("imageUrl2"));

		verify(userService, times(1)).getUserInfo(userId);
	}
}