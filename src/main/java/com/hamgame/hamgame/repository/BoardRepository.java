package com.hamgame.hamgame.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.type.BoardCategory;

public interface BoardRepository extends JpaRepository<Board, Long> {
	Page<Board> findByGame_GameId(Long gameId, Pageable pageable);

	Optional<Board> findByBoardIdAndGame_GameId(Long boardId, Long gameId);

	Page<Board> findByBoardCategoryAndGame_GameId(BoardCategory boardCategory, Long gameId, Pageable pageable);
}
