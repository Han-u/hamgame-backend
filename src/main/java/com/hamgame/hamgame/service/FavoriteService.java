package com.hamgame.hamgame.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.dto.AddFavGamesReq;
import com.hamgame.hamgame.dto.RemoveFavGamesReq;
import com.hamgame.hamgame.dto.UpdateFavGamesReq;
import com.hamgame.hamgame.repository.GameRepository;
import com.hamgame.hamgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final UserRepository userRepository;

	private final GameRepository gameRepository;

	public Set<Game> getFavoriteGameList() {
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
		return user.getGames();
	}

	@Transactional
	public void addGame(AddFavGamesReq requestDto) {
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
		List<Game> games = gameRepository.findByGameIdIn(requestDto.getGameIds());
		user.addGames(games);
	}

	@Transactional
	public void removeGame(RemoveFavGamesReq requestDto) {
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
		user.removeGame(requestDto.getGameId());
	}

	@Transactional
	public void updateGames(UpdateFavGamesReq requestDto) {
		List<Game> games = gameRepository.findByGameIdIn(requestDto.getGameIds());
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
		user.updateGames(games);
	}

}
