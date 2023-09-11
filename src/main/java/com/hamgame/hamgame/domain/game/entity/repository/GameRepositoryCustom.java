package com.hamgame.hamgame.domain.game.entity.repository;

import java.util.List;

import com.hamgame.hamgame.domain.game.dto.GameDto;

public interface GameRepositoryCustom {
	List<GameDto> findFavoriteGamesByUser(Long userId);
}
