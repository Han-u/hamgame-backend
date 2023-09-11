package com.hamgame.hamgame.domain.board.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.board.dto.BoardDto;
import com.hamgame.hamgame.domain.board.dto.BoardListDto;
import com.hamgame.hamgame.domain.board.dto.BoardSaveRequest;
import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.board.entity.repository.BoardRepository;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.repository.GameRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final GameRepository gameRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public Page<BoardListDto> getBoardList(Long gameId, Pageable pageable, BoardCategory boardCategory) {
		return boardRepository.getBoardListPage(pageable, gameId, boardCategory);
	}

	@Transactional(readOnly = true)
	public BoardDto getBoard(Long gameId, Long boardId) {
		Board board = boardRepository.findByBoardId(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		return BoardDto.of(board);
	}

	@Transactional
	public void createBoard(Long gameId, BoardSaveRequest boardSaveRequest, Long userId) {
		Game game = gameRepository.findById(gameId).orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));
		User user = userRepository.getReferenceById(userId);
		Board board = boardSaveRequest.toEntity(game, user);
		boardRepository.save(board);
	}

	@Transactional
	public void updateBoard(Long gameId, Long boardId, BoardSaveRequest boardSaveRequest, Long userId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

		isAuthorOfPost(board, userId);

		board.updateBoard(boardSaveRequest.getTitle(), boardSaveRequest.getContent(), boardSaveRequest.getImage(),
			boardSaveRequest.getBoardCategory());
	}

	@Transactional
	public void deleteBoard(Long gameId, Long boardId, Long userId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

		isAuthorOfPost(board, userId);

		boardRepository.delete(board);
	}

	public void isAuthorOfPost(Board board, Long userId) {
		if (!Objects.equals(board.getUser().getId(), userId)) {
			throw new CustomException(ErrorCode.NOT_POST_AUTHOR);
		}
	}
}
