package com.hamgame.hamgame.domain.board.entity.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hamgame.hamgame.domain.board.dto.BoardListDto;
import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;

public interface BoardRepositoryCustom {
	Optional<Board> findByBoardIdAndGame_GameId(Long boardId, Long gameId);

	Optional<Board> findByBoardId(Long boardId);

	Page<BoardListDto> getBoardListPage(Pageable pageable, Long gameId, BoardCategory category);

}
