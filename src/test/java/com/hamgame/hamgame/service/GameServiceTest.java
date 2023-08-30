package com.hamgame.hamgame.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.repository.GameRepository;
import com.hamgame.hamgame.domain.game.service.GameService;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

	@InjectMocks
	GameService gameService;

	@Mock
	GameRepository gameRepository;

	private List<Game> getGames(List<Long> ids) {
		List<Game> games = new ArrayList<>();
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

	@Test
	@DisplayName("게임 조회 - 성공")
	void getGameList() {
		// Given
		List<Game> games = getGames(Arrays.asList(1L, 2L, 3L));
		List<GameDto> expected = games.stream().map(GameDto::of).collect(Collectors.toList());

		when(gameRepository.findAll()).thenReturn(games);

		// When
		List<GameDto> actual = gameService.getGameList();

		// Then
		verify(gameRepository, times(1)).findAll();

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}
}