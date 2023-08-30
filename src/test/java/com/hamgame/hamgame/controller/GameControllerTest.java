package com.hamgame.hamgame.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.web.servlet.MockMvc;

import com.hamgame.hamgame.config.TestSecurityConfig;
import com.hamgame.hamgame.domain.game.controller.GameController;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.game.service.GameService;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(value = GameController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)

	})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class GameControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	GameService gameService;

	@Test
	@DisplayName("게임 목록 - 성공")
	void gameList() throws Exception {
		// Given
		List<GameDto> result = Arrays.asList(
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
		when(gameService.getGameList()).thenReturn(result);

		// When & Then
		mockMvc.perform(get("/games"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].gameId").value(1L))
			.andExpect(jsonPath("$[0].name").value("game1"))
			.andExpect(jsonPath("$[1].gameId").value(2L))
			.andExpect(jsonPath("$[1].name").value("game2"));

		verify(gameService, times(1)).getGameList();
	}

}