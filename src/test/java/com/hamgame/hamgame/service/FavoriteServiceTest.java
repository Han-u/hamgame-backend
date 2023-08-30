package com.hamgame.hamgame.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamgame.hamgame.domain.favorite.dto.FavAddRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavRemoveRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavUpdateRequest;
import com.hamgame.hamgame.domain.favorite.service.FavoriteService;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.repository.GameRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	@InjectMocks
	FavoriteService favoriteService;

	@Mock
	UserRepository userRepository;

	@Mock
	GameRepository gameRepository;

	Long userId = 1L;

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

	private User getUser() {
		return User.builder()
			.id(userId)
			.name("user1")
			.games(new HashSet<>())
			.build();
	}

	@Test
	@DisplayName("즐겨찾기 게임 조회 - 성공")
	void getFavoriteGameList() {
		// Given
		User user = getUser();
		List<Game> games = getGames(Arrays.asList(1L, 2L, 3L));
		user.addGames(games);
		List<GameDto> expected = user.getGames().stream().map(GameDto::of).collect(Collectors.toList());

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		// When
		List<GameDto> actual = favoriteService.getFavoriteGameList(userId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("즐겨찾기 게임 조회 - 성공 (즐겨찾는 게임 없음)")
	void getFavoriteGameListNoGames() {
		// Given
		User user = getUser();
		List<GameDto> expected = user.getGames().stream().map(GameDto::of).collect(Collectors.toList());

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		// When
		List<GameDto> actual = favoriteService.getFavoriteGameList(userId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("즐겨찾기 게임 조회 실패 - 유저 없음")
	void getFavoriteGameListNoGamesNoUser() {
		// Given
		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> favoriteService.getFavoriteGameList(userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		verify(userRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("즐겨찾기 게임 추가 - 성공")
	void addGame() {
		// Given
		List<Long> ids = Arrays.asList(1L, 2L, 3L);
		FavAddRequest request = new FavAddRequest(ids);
		User user = getUser();
		List<Game> games = getGames(ids);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(gameRepository.findByGameIdIn(anyList())).thenReturn(games);

		// When
		user.addGames(games);
		favoriteService.addGame(request, userId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());

		assertThat(user.getGames().equals(Set.copyOf(games))).isTrue();
	}

	@Test
	@DisplayName("즐겨찾기 게임 추가 실패 - 유저 없음")
	void addGameNoUser() {
		// Given
		List<Long> ids = Arrays.asList(1L, 2L, 3L);
		FavAddRequest request = new FavAddRequest(ids);

		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> favoriteService.addGame(request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		verify(userRepository, times(1)).findById(anyLong());
		verifyNoInteractions(gameRepository);
	}

	@Test
	@DisplayName("즐겨찾기 게임 수정 - 성공")
	void updateGames() {
		// Given
		List<Long> defaultGameIds = Arrays.asList(1L, 2L, 3L);
		List<Long> newGameIds = Arrays.asList(7L, 8L);
		FavUpdateRequest request = new FavUpdateRequest(newGameIds);
		User user = getUser();
		List<Game> games = getGames(defaultGameIds);
		List<Game> newGames = getGames(newGameIds);
		user.addGames(games);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(gameRepository.findByGameIdIn(anyList())).thenReturn(newGames);

		// When
		user.updateGames(newGames);
		favoriteService.updateGames(request, userId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		verify(gameRepository, times(1)).findByGameIdIn(anyList());
		assertThat(user.getGames().equals(Set.copyOf(newGames))).isTrue();
	}

	@Test
	@DisplayName("즐겨찾기 게임 수정 실패 - 유저 없음")
	void updateGamesNoUser() {
		// Given
		List<Long> newGameIds = Arrays.asList(7L, 8L);
		FavUpdateRequest request = new FavUpdateRequest(newGameIds);

		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> favoriteService.updateGames(request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		verifyNoInteractions(gameRepository);
	}

	@Test
	@DisplayName("즐겨찾기 게임 삭제 - 성공")
	void removeGame() {
		// Given
		List<Long> defaultGameIds = Arrays.asList(1L, 2L, 3L);
		Long removeId = 1L;
		FavRemoveRequest request = new FavRemoveRequest(removeId);
		User user = getUser();
		List<Game> games = getGames(defaultGameIds);
		user.addGames(games);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		// When
		user.removeGame(removeId);
		favoriteService.removeGame(request, userId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		assertThat(user.getGames().stream().map(Game::getGameId).collect(Collectors.toSet())).isEqualTo(
			Set.of(2L, 3L));
	}

	@Test
	@DisplayName("즐겨찾기 게임 삭제 실패 - 유저 없음")
	void removeGameNoUser() {
		// Given
		Long removeId = 1L;
		FavRemoveRequest request = new FavRemoveRequest(removeId);

		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> favoriteService.removeGame(request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
	}

}