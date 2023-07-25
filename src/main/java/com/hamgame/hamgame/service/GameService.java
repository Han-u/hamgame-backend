package com.hamgame.hamgame.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.dto.GameDto;
import com.hamgame.hamgame.repository.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {
	private final GameRepository gameRepository;

	public List<GameDto> getGameList() {
		List<Game> games = gameRepository.findAll();
		return GameDto.of(games);
	}
}
