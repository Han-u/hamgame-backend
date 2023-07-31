package com.hamgame.hamgame.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.dto.AddFavGamesReq;
import com.hamgame.hamgame.dto.GameDto;
import com.hamgame.hamgame.dto.RemoveFavGamesReq;
import com.hamgame.hamgame.dto.UpdateFavGamesReq;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.ErrorCode;
import com.hamgame.hamgame.repository.GameRepository;
import com.hamgame.hamgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final UserRepository userRepository;

	private final GameRepository gameRepository;

	public List<GameDto> getFavoriteGameList(UserPrincipal userPrincipal) {
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return user.getGames().stream().map(GameDto::of).collect(Collectors.toList());
	}

	@Transactional
	public void addGame(AddFavGamesReq requestDto, UserPrincipal userPrincipal) {
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		List<Game> games = gameRepository.findByGameIdIn(requestDto.getGameIds());
		user.addGames(games);
	}

	@Transactional
	public void removeGame(RemoveFavGamesReq requestDto, UserPrincipal userPrincipal) {
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		user.removeGame(requestDto.getGameId());
	}

	@Transactional
	public void updateGames(UpdateFavGamesReq requestDto, UserPrincipal userPrincipal) {
		List<Game> games = gameRepository.findByGameIdIn(requestDto.getGameIds());
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		user.updateGames(games);
	}

}
