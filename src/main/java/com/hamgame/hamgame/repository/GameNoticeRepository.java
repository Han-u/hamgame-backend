package com.hamgame.hamgame.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.GameNotice;

public interface GameNoticeRepository extends JpaRepository<GameNotice, Long> {
	Page<GameNotice> findByGameIn(Set<Game> games, Pageable pageable);
}
