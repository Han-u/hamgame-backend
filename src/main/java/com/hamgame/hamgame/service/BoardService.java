package com.hamgame.hamgame.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.domain.type.BoardCategory;
import com.hamgame.hamgame.dto.BoardDto;
import com.hamgame.hamgame.dto.BoardListDto;
import com.hamgame.hamgame.dto.BoardSaveReq;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.ErrorCode;
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
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		return BoardDto.of(board);
	}

	public void createBoard(Long gameId, BoardSaveReq boardSaveReq, UserPrincipal userPrincipal) {
		Game game = gameRepository.findById(gameId).orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Board board = boardSaveReq.toEntity(game, user);
		boardRepository.save(board);
	}

	@Transactional
	public void updateBoard(Long gameId, Long boardId, BoardSaveReq boardSaveReq, UserPrincipal userPrincipal) {
		Board board = boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		isAuthorOfPost(board, user);

		board.updateBoard(boardSaveReq.getTitle(), boardSaveReq.getContent(), boardSaveReq.getImage(),
			boardSaveReq.getBoardCategory());
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
