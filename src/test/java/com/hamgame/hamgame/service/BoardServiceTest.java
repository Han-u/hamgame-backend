package com.hamgame.hamgame.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hamgame.hamgame.domain.board.dto.BoardDto;
import com.hamgame.hamgame.domain.board.dto.BoardListDto;
import com.hamgame.hamgame.domain.board.dto.BoardSaveRequest;
import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.board.entity.repository.BoardRepository;
import com.hamgame.hamgame.domain.board.service.BoardService;
import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.repository.GameRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

	@InjectMocks
	BoardService boardService;

	@Mock
	BoardRepository boardRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	GameRepository gameRepository;

	Long userId = 1L;
	Long gameId = 11L;
	Long boardId = 2L;

	BoardSaveRequest getRequest() {
		return BoardSaveRequest.builder()
			.title("title")
			.content("content")
			.boardCategory(BoardCategory.DEFAULT)
			.build();
	}

	User getUser(Long i) {
		return User.builder()
			.id(i)
			.email("email" + i)
			.nickname("nickname" + i)
			.build();
	}

	Game getGame() {
		return Game.builder()
			.gameId(gameId)
			.name("name")
			.build();
	}

	Board getBoardEntity() {
		return Board.builder()
			.boardId(boardId)
			.title("title")
			.content("content")
			.image("image")
			.viewCount(1000)
			.boardCategory(BoardCategory.DEFAULT)
			.comment(getCommentEntity(3))
			.user(getUser(userId))
			.build();
	}

	List<Comment> getCommentEntity(int idx) {
		List<Comment> comments = new ArrayList<>();
		for (int i = 1 + idx; i <= 3 + idx; i++) {
			comments.add(
				Comment.builder()
					.commentId((long)i)
					.comment("comment" + i)
					.user(getUser((long)i)).build()
			);
		}
		return comments;
	}

	List<Board> getBoardListEntity() {
		List<Board> boards = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			boards.add(
				Board.builder()
					.boardId((long)i)
					.title("title" + i)
					.content("content" + i)
					.image("image" + i)
					.viewCount(i)
					.boardCategory(BoardCategory.DEFAULT)
					.user(getUser((long)i))
					.comment(getCommentEntity(i))
					.build()
			);
		}
		return boards;
	}

	@Test
	@DisplayName("게시판 조회 - 카테고리별 성공")
	void getBoardList() {
		// Given
		List<Board> boards = getBoardListEntity();
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "boardId");
		Page<Board> result = new PageImpl<>(boards, pageable, boards.size());

		when(boardRepository.findByBoardCategoryAndGame_GameId(BoardCategory.DEFAULT, gameId, pageable)).thenReturn(
			result);

		// When
		Page<BoardListDto> actual = boardService.getBoardList(gameId, pageable, BoardCategory.DEFAULT);

		// Then
		verify(boardRepository, times(1)).findByBoardCategoryAndGame_GameId(eq(BoardCategory.DEFAULT), eq(gameId),
			eq(pageable));

		assertThat(actual).usingRecursiveComparison().isEqualTo(result.map(BoardListDto::of));
	}

	@Test
	@DisplayName("게시판 조회 - 성공")
	void getBoardListNoCategory() {
		// Given
		List<Board> boards = getBoardListEntity();
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "boardId");
		Page<Board> result = new PageImpl<>(boards, pageable, boards.size());

		when(boardRepository.findByGame_GameId(gameId, pageable)).thenReturn(result);

		// When
		Page<BoardListDto> actual = boardService.getBoardList(gameId, pageable, null);

		// Then
		verify(boardRepository, times(1)).findByGame_GameId(eq(gameId), eq(pageable));

		assertThat(actual).usingRecursiveComparison().isEqualTo(result.map(BoardListDto::of));
	}

	@Test
	@DisplayName("게시판 개별 조회 - 성공")
	void getBoard() {
		// Given
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.ofNullable(board));

		// When
		BoardDto actual = boardService.getBoard(gameId, boardId);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(eq(boardId), eq(gameId));

		assertThat(actual).usingRecursiveComparison().isEqualTo(BoardDto.of(board));
	}

	@Test
	@DisplayName("게시판 개별 조회 실패 - 게시글 없음(삭제)")
	void getBoardNoBoard() {
		// Given
		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenThrow(
			new CustomException(ErrorCode.BOARD_NOT_FOUND));

		// When
		assertThatThrownBy(() -> boardService.getBoard(gameId, boardId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(eq(boardId), eq(gameId));
	}

	@Test
	@DisplayName("게시판 작성 - 성공")
	void createBoard() {
		// Given
		BoardSaveRequest request = getRequest();
		Game game = getGame();
		User user = getUser(userId);

		when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// When
		boardService.createBoard(gameId, request, userId);

		// Then
		verify(gameRepository, times(1)).findById(anyLong());
		verify(userRepository, times(1)).findById(anyLong());
		verify(boardRepository, times(1)).save(any(Board.class));
	}

	@Test
	@DisplayName("게시판 작성 실패 - 게임 없음")
	void createBoardNoGame() {
		// Given
		BoardSaveRequest request = getRequest();
		when(gameRepository.findById(gameId)).thenThrow(new CustomException(ErrorCode.GAME_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> boardService.createBoard(gameId, request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.GAME_NOT_FOUND);

		// Then
		verify(gameRepository, times(1)).findById(anyLong());
		verifyNoInteractions(userRepository);
		verifyNoInteractions(boardRepository);
	}

	@Test
	@DisplayName("게시판 작성 - 유저 없음")
	void createBoardNoUser() {
		// Given
		BoardSaveRequest request = getRequest();
		Game game = getGame();

		when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
		when(userRepository.findById(userId)).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When
		assertThatThrownBy(() -> boardService.createBoard(gameId, request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(gameRepository, times(1)).findById(anyLong());
		verify(userRepository, times(1)).findById(anyLong());
		verifyNoInteractions(boardRepository);
	}

	@Test
	@DisplayName("게시판 수정 - 성공")
	void updateBoard() {
		// Given
		BoardSaveRequest request = getRequest();
		User user = getUser(userId);
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.of(board));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// When
		boardService.updateBoard(gameId, boardId, request, userId);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verify(userRepository, times(1)).findById(userId);
		assertThat(Objects.equals(board.getUser().getId(), user.getId())).isTrue();
	}

	@Test
	@DisplayName("게시판 수정 실패 - 게시글 없음")
	void updateBoardNoBoard() {
		// Given
		BoardSaveRequest request = getRequest();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenThrow(
			new CustomException(ErrorCode.BOARD_NOT_FOUND));

		// When
		assertThatThrownBy(() -> boardService.updateBoard(gameId, boardId, request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("게시판 수정 실패 - 유저 없음")
	void updateBoardNoUser() {
		// Given
		BoardSaveRequest request = getRequest();
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.of(board));
		when(userRepository.findById(userId)).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When
		assertThatThrownBy(() -> boardService.updateBoard(gameId, boardId, request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verify(userRepository, times(1)).findById(userId);
	}

	@Test
	@DisplayName("게시판 수정 실패 - 작성자 불일치")
	void updateBoardNotAuthor() {
		// Given
		BoardSaveRequest request = getRequest();
		User user = getUser(userId + 1);
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.of(board));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// When
		assertThatThrownBy(() -> boardService.updateBoard(gameId, boardId, request, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BAD_REQUEST);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verify(userRepository, times(1)).findById(userId);
		assertThat(Objects.equals(board.getUser().getId(), user.getId())).isFalse();
	}

	@Test
	@DisplayName("게시판 삭제 - 성공")
	void deleteBoard() {
		// Given
		User user = getUser(userId);
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.of(board));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// When
		boardService.deleteBoard(gameId, boardId, userId);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verify(userRepository, times(1)).findById(userId);
		verify(boardRepository, times(1)).delete(any(Board.class));
		assertThat(Objects.equals(board.getUser().getId(), user.getId())).isTrue();
	}

	@Test
	@DisplayName("게시판 삭제 실패 - 게시글 없음")
	void deleteBoardNoBoard() {
		// Given
		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenThrow(
			new CustomException(ErrorCode.BOARD_NOT_FOUND));

		// When
		assertThatThrownBy(() -> boardService.deleteBoard(gameId, boardId, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("게시판 삭제 실패 - 유저 없음")
	void deleteBoardNoUser() {
		// Given
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.of(board));
		when(userRepository.findById(userId)).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When
		assertThatThrownBy(() -> boardService.deleteBoard(gameId, boardId, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verify(userRepository, times(1)).findById(userId);
	}

	@Test
	@DisplayName("게시판 삭제 실패 - 작성자 불일치")
	void deleteBoardNotAuthor() {
		// Given
		User user = getUser(userId + 1);
		Board board = getBoardEntity();

		when(boardRepository.findByBoardIdAndGame_GameId(boardId, gameId)).thenReturn(Optional.of(board));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// When
		assertThatThrownBy(() -> boardService.deleteBoard(gameId, boardId, userId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BAD_REQUEST);

		// Then
		verify(boardRepository, times(1)).findByBoardIdAndGame_GameId(boardId, gameId);
		verify(userRepository, times(1)).findById(userId);
		assertThat(Objects.equals(board.getUser().getId(), user.getId())).isFalse();
	}

}