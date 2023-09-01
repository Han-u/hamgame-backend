package com.hamgame.hamgame.domain.favorite.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.favorite.dto.FavAddRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavRemoveRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavUpdateRequest;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.repository.GameRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final UserRepository userRepository;

	private final GameRepository gameRepository;

	@Transactional(readOnly = true)
	@Cacheable(value = "favoriteGames", key = "#userId")
	public List<GameDto> getFavoriteGameList(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return user.getGames().stream().map(GameDto::of).collect(Collectors.toList());
	}

	@Transactional
	@Caching(
		evict = {
			@CacheEvict(value = "favoriteGames", key = "#userId"),
			@CacheEvict(value = "favoriteGameNotice", key = "#userId")
		}
	)
	public void addGame(FavAddRequest requestDto, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		List<Game> games = gameRepository.findByGameIdIn(requestDto.getGameIds());
		user.addGames(games);
	}

	@Transactional
	@Caching(
		evict = {
			@CacheEvict(value = "favoriteGames", key = "#userId"),
			@CacheEvict(value = "favoriteGameNotice", key = "#userId")
		}
	)
	public void updateGames(FavUpdateRequest requestDto, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		List<Game> games = gameRepository.findByGameIdIn(requestDto.getGameIds());
		user.updateGames(games);
	}

	@Transactional
	@Caching(
		evict = {
			@CacheEvict(value = "favoriteGames", key = "#userId"),
			@CacheEvict(value = "favoriteGameNotice", key = "#userId")
		}
	)
	public void removeGame(FavRemoveRequest requestDto, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		user.removeGame(requestDto.getGameId());
	}

}
