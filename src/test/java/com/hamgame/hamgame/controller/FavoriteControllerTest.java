package com.hamgame.hamgame.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.hamgame.hamgame.domain.favorite.controller.FavoriteController;
import com.hamgame.hamgame.domain.favorite.dto.FavAddRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavRemoveRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavUpdateRequest;
import com.hamgame.hamgame.domain.favorite.service.FavoriteService;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(value = FavoriteController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)

	})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class FavoriteControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	FavoriteService favoriteService;

	Long userId = 1L;

	ObjectMapper objectMapper = new ObjectMapper();

	public List<GameDto> createFavDto() {
		return Arrays.asList(
			GameDto.builder()
				.gameId(1L)
				.name("game1")
				.imageUrl("image1")
				.homepageUrl("url1")
				.build(),
			GameDto.builder()
				.gameId(2L)
				.name("game2")
				.imageUrl("image2")
				.homepageUrl("url2")
				.build()
		);
	}

	@Test
	@DisplayName("즐겨찾기 게임 조회 - 성공")
	@WithCustomMockUser
	void getFavoriteGameList() throws Exception {
		// Given
		List<GameDto> result = createFavDto();
		when(favoriteService.getFavoriteGameList(userId)).thenReturn(result);

		// When & Then
		mockMvc.perform(get("/favorites"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].gameId").value(1L))
			.andExpect(jsonPath("$[0].name").value("game1"))
			.andExpect(jsonPath("$[1].gameId").value(2L))
			.andExpect(jsonPath("$[1].name").value("game2"));

		verify(favoriteService, times(1)).getFavoriteGameList(eq(userId));

	}

	@Test
	@DisplayName("즐겨찾기 게임 추가 - 성공")
	@WithCustomMockUser
	void addFavoriteGames() throws Exception {
		// Given
		FavAddRequest request = new FavAddRequest(Arrays.asList(1L, 2L, 3L));
		doNothing().when(favoriteService).addGame(request, userId);

		// When & Then
		mockMvc.perform(post("/favorites")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
		verify(favoriteService, times(1)).addGame(any(FavAddRequest.class), eq(userId));
	}

	@Test
	@DisplayName("즐겨찾기 게임 추가 - 실패")
	@WithCustomMockUser
	void addFavoriteGamesFail() throws Exception {
		// Given
		FavAddRequest request = new FavAddRequest(new ArrayList<>());

		// When & Then
		mockMvc.perform(post("/favorites")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(favoriteService);
	}

	@Test
	@DisplayName("즐겨찾기 게임 수정 - 성공")
	@WithCustomMockUser
	void updateFavoriteGames() throws Exception {
		// Given
		FavUpdateRequest request = new FavUpdateRequest(Arrays.asList(1L, 2L, 3L));
		doNothing().when(favoriteService).updateGames(request, userId);

		// When & Then
		mockMvc.perform(put("/favorites")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
		verify(favoriteService, times(1)).updateGames(any(FavUpdateRequest.class), eq(userId));
	}

	@Test
	@DisplayName("즐겨찾기 게임 수정 - 실패")
	@WithCustomMockUser
	void updateFavoriteGamesFail() throws Exception {
		// Given
		FavUpdateRequest request = new FavUpdateRequest(new ArrayList<>());
		System.out.println();

		// When & Then
		mockMvc.perform(put("/favorites")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		;

		verifyNoInteractions(favoriteService);
	}

	@Test
	@DisplayName("즐겨찾기 게임 삭제 - 성공")
	@WithCustomMockUser
	void removeGames() throws Exception {
		// Given
		FavRemoveRequest request = new FavRemoveRequest(1L);
		doNothing().when(favoriteService).removeGame(request, userId);

		// When & Then
		mockMvc.perform(delete("/favorites")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
		verify(favoriteService, times(1)).removeGame(any(FavRemoveRequest.class), eq(userId));

	}

	@Test
	@DisplayName("즐겨찾기 게임 삭제 - 실패")
	@WithCustomMockUser
	void removeGamesFail() throws Exception {
		// Given
		FavRemoveRequest request = new FavRemoveRequest(null);

		// When & Then
		mockMvc.perform(delete("/favorites")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

		verifyNoInteractions(favoriteService);
	}
}