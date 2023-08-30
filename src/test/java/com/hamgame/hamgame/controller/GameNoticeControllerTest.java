package com.hamgame.hamgame.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import com.hamgame.hamgame.config.TestSecurityConfig;
import com.hamgame.hamgame.config.WithCustomMockUser;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.gameNotice.controller.GameNoticeController;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.service.GameNoticeService;
import com.hamgame.hamgame.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(value = GameNoticeController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)

	})
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class GameNoticeControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	GameNoticeService gameNoticeService;

	@Test
	@WithCustomMockUser
	@DisplayName("즐겨찾기 게임 공지 조회 - 성공")
	void getMyGameNoticeList() throws Exception {
		// Given
		Long userId = 1L;
		List<GameNoticeDto> notices = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			notices.add(
				GameNoticeDto.builder()
					.gameNoticeId((long)i)
					.title("title" + i)
					.noticeType("type" + i)
					.noticeUrl("noticeUrl" + i)
					.imageUrl("imageUrl" + i)
					.postCreatedAt(LocalDateTime.now())
					.game(GameDto.builder().gameId((long)i).name("game" + i).build())
					.build()
			);
		}
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Page<GameNoticeDto> pageResult = new PageImpl<>(notices, pageable, notices.size());

		when(gameNoticeService.getMyGameNoticeList(pageable, userId)).thenReturn(pageResult);

		// When & Then
		mockMvc.perform(get("/notices")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.number").value(0))
			.andExpect(jsonPath("$.size").value(10))
			.andExpect(jsonPath("$.content.[*].gameNoticeId", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.content.[*].title", containsInAnyOrder("title1", "title2", "title3")))
			.andExpect(jsonPath("$.content.[*].noticeType", containsInAnyOrder("type1", "type2", "type3")))
			.andExpect(
				jsonPath("$.content.[*].noticeUrl", containsInAnyOrder("noticeUrl1", "noticeUrl2", "noticeUrl3")))
			.andExpect(jsonPath("$.content.[*].imageUrl", containsInAnyOrder("imageUrl1", "imageUrl2", "imageUrl3")))
			.andExpect(jsonPath("$.content.[*].game.gameId", containsInAnyOrder(1, 2, 3)))
			.andExpect(jsonPath("$.content.[*].game.name", containsInAnyOrder("game1", "game2", "game3")));

		verify(gameNoticeService, times(1)).getMyGameNoticeList(pageable, userId);
	}
}