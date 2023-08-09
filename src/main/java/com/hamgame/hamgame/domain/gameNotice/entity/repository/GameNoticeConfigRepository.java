package com.hamgame.hamgame.domain.gameNotice.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hamgame.hamgame.domain.gameNotice.entity.GameNoticeConfig;

public interface GameNoticeConfigRepository extends JpaRepository<GameNoticeConfig, Long> {

	@Query("select c from GameNoticeConfig c join fetch c.game")
	List<GameNoticeConfig> findAll();
}
