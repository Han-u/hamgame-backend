package com.hamgame.hamgame.domain.board.entity.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;

public interface BoardRepository extends JpaRepository<Board, Long> {
	Page<Board> findByGame_GameId(Long gameId, Pageable pageable);

	Optional<Board> findByBoardIdAndGame_GameId(Long boardId, Long gameId);

	Page<Board> findByBoardCategoryAndGame_GameId(BoardCategory boardCategory, Long gameId, Pageable pageable);
}
