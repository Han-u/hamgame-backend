package com.hamgame.hamgame.domain.game.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.game.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom {
	List<Game> findByGameIdIn(List<Long> ids);
}
