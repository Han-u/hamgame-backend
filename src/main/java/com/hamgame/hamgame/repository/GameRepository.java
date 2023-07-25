package com.hamgame.hamgame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
	List<Game> findByGameIdIn(List<Long> ids);
}
