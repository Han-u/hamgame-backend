package com.hamgame.hamgame.domain.game.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.repository.GameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {
	private final GameRepository gameRepository;

	@Transactional(readOnly = true)
	public List<GameDto> getGameList() {
		List<Game> games = gameRepository.findAll();
		return games.stream().map(GameDto::of).collect(Collectors.toList());
	}
}
