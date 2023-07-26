package com.hamgame.hamgame.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.domain.type.BoardCategory;
import com.hamgame.hamgame.dto.BoardDto;
import com.hamgame.hamgame.dto.BoardListDto;
import com.hamgame.hamgame.dto.BoardSaveReq;
import com.hamgame.hamgame.repository.BoardRepository;
import com.hamgame.hamgame.repository.GameRepository;
import com.hamgame.hamgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final GameRepository gameRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	public Page<BoardListDto> getBoardList(Long gameId, Pageable pageable, BoardCategory boardCategory) {
		if (boardCategory == null) {
			return boardRepository.findByGame_GameId(gameId, pageable).map(BoardListDto::of);
		} else {
			return boardRepository.findByBoardCategoryAndGame_GameId(boardCategory, gameId, pageable)
				.map(BoardListDto::of);
		}
	}

	public BoardDto getBoard(Long gameId, Long boardId) {
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId);
		return BoardDto.of(board);
	}

	public void createBoard(Long gameId, BoardSaveReq boardSaveReq) {
		Game game = gameRepository.findById(gameId).orElseThrow(RuntimeException::new);
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new); // 임시

		Board board = boardSaveReq.toEntity(game, user);
		boardRepository.save(board);
	}

	@Transactional
	public void updateBoard(Long gameId, Long boardId, BoardSaveReq boardSaveReq) {
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId);
		board.updateBoard(boardSaveReq.getTitle(), boardSaveReq.getContent(), boardSaveReq.getImage(),
			boardSaveReq.getBoardCategory());
	}

	public void deleteBoard(Long gameId, Long boardId) {
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId);
		boardRepository.delete(board);
	}
}
