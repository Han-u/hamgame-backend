package com.hamgame.hamgame.domain.gameNotice.entity.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNotice;

public interface GameNoticeRepository extends JpaRepository<GameNotice, Long> {
	Page<GameNotice> findByGameIn(Set<Game> games, Pageable pageable);
}
