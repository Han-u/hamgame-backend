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
import com.hamgame.hamgame.security.auth.UserPrincipal;

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
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		return BoardDto.of(board);
	}

	public void createBoard(Long gameId, BoardSaveRequest boardSaveRequest, UserPrincipal userPrincipal) {
		Game game = gameRepository.findById(gameId).orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Board board = boardSaveRequest.toEntity(game, user);
		boardRepository.save(board);
	}

	@Transactional
	public void updateBoard(Long gameId, Long boardId, BoardSaveRequest boardSaveRequest, UserPrincipal userPrincipal) {
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		isAuthorOfPost(board, user);

		board.updateBoard(boardSaveRequest.getTitle(), boardSaveRequest.getContent(), boardSaveRequest.getImage(),
			boardSaveRequest.getBoardCategory());
	}

	public void deleteBoard(Long gameId, Long boardId, UserPrincipal userPrincipal) {
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		isAuthorOfPost(board, user);
		boardRepository.delete(board);
	}

	public void isAuthorOfPost(Board board, User user) {
		if (!Objects.equals(board.getUser().getId(), user.getId())) {
			throw new CustomException(ErrorCode.BAD_REQUEST);
		}
	}
}