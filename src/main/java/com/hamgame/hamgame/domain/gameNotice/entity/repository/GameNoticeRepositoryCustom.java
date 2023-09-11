package com.hamgame.hamgame.domain.gameNotice.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;

public interface GameNoticeRepositoryCustom {
	Page<GameNoticeDto> findFavoriteNoticeByUserId(Long userId, Pageable pageable);
}
