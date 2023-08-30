package com.hamgame.hamgame.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNotice;
import com.hamgame.hamgame.domain.gameNotice.entity.repository.GameNoticeRepository;
import com.hamgame.hamgame.domain.gameNotice.service.GameNoticeService;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

@ExtendWith(MockitoExtension.class)
class GameNoticeServiceTest {
	@InjectMocks
	GameNoticeService gameNoticeService;

	@Mock
	UserRepository userRepository;

	@Mock
	GameNoticeRepository gameNoticeRepository;

	private Set<Game> getGames(List<Long> ids) {
		Set<Game> games = new HashSet<>();
		for (Long id : ids) {
			games.add(
				Game.builder()
					.gameId(id)
					.name("game" + id)
					.imageUrl("image" + id)
					.homepageUrl("url" + id)
					.build()
			);
		}
		return games;
	}

	private User getUser() {
		return User.builder()
			.id(1L)
			.name("user1")
			.games(getGames(Arrays.asList(1L, 2L, 3L)))
			.build();
	}

	@Test
	@DisplayName("즐겨찾기 게임 공지 조회 - 성공")
	void getMyGameNoticeList() {
		// Given
		User user = getUser();
		List<GameNotice> notices = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			notices.add(
				GameNotice.builder()
					.gameNoticeId((long)i)
					.title("title" + i)
					.noticeType("type" + i)
					.noticeUrl("noticeUrl" + i)
					.imageUrl("imageUrl" + i)
					.postCreatedAt(LocalDateTime.now())
					.game(Game.builder().gameId((long)i).name("game" + i).build())
					.build()
			);
		}
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
		Page<GameNotice> pageResult = new PageImpl<>(notices, pageable, notices.size());

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(gameNoticeRepository.findByGameIn(user.getGames(), pageable)).thenReturn(pageResult);

		// When
		Page<GameNoticeDto> actual = gameNoticeService.getMyGameNoticeList(pageable, 1L);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		verify(gameNoticeRepository, times(1)).findByGameIn(user.getGames(), pageable);

		assertThat(actual).usingRecursiveComparison().isEqualTo(pageResult.map(GameNoticeDto::of));
	}

	@Test
	@DisplayName("즐겨찾기 게임 공지 조회 실패 - 유저 없음")
	void getMyGameNoticeListNoUser() {
		// Given
		User user = getUser();
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> gameNoticeService.getMyGameNoticeList(pageable, 1L))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		verify(userRepository, times(1)).findById(anyLong());
		verifyNoInteractions(gameNoticeRepository);
	}
}