package com.hamgame.hamgame.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.repository.BoardRepository;
import com.hamgame.hamgame.domain.comment.dto.CommentSaveRequest;
import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.comment.entity.repository.CommentRepository;
import com.hamgame.hamgame.domain.comment.service.CommentService;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	CommentService commentService;

	@Mock
	CommentRepository commentRepository;

	@Mock
	BoardRepository boardRepository;

	@Mock
	UserRepository userRepository;

	Long requestUserId = 1L;
	Long authorUserId = 2L;
	Long boardId = 11L;
	Long commentId = 111L;

	private CommentSaveRequest createSaveRequest() {
		return CommentSaveRequest.builder()
			.comment("new comment")
			.build();
	}

	private User createUser(Long id) {
		return User.builder().id(id).build();
	}

	private Board createBoard(Long id) {
		return Board.builder().boardId(id).build();
	}

	private Comment createComment(Long id, Board board, User user) {
		return Comment.builder().commentId(id).comment("old comment").board(board).user(user).build();
	}

	@Test
	@DisplayName("댓글 작성 - 성공")
	void createCommentSuccess() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();

		User user = createUser(requestUserId);
		Board board = createBoard(boardId);
		Comment comment = commentSaveRequest.toEntity(board, user);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);

		// When
		commentService.createComment(boardId, commentSaveRequest, requestUserId);

		// Then
		verify(userRepository, times(1)).findById(anyLong());
		verify(boardRepository, times(1)).findById(anyLong());
		verify(commentRepository, times(1)).save(any(Comment.class));

		assertThat(comment.getBoard()).isEqualTo(board);
		assertThat(comment.getUser()).isEqualTo(user);
	}

	@Test
	@DisplayName("댓글 작성 - 유저 없음")
	void createCommentNoUser() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();

		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> commentService.createComment(boardId, commentSaveRequest, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		verify(userRepository, times(1)).findById(anyLong());
		verifyNoInteractions(boardRepository);
		verifyNoInteractions(commentRepository);
	}

	@Test
	@DisplayName("댓글 작성 - 게시글 없음")
	void createCommentFailNoBoard() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();
		User user = createUser(requestUserId);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(boardRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.BOARD_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> commentService.createComment(boardId, commentSaveRequest, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);

		verify(userRepository, times(1)).findById(anyLong());
		verify(boardRepository, times(1)).findById(boardId);
		verifyNoInteractions(commentRepository);
	}

	@Test
	@DisplayName("댓글 수정 - 성공")
	void updateCommentSuccess() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();

		User user = createUser(requestUserId);
		Board board = createBoard(boardId);
		Comment comment = createComment(commentId, board, user);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenReturn(Optional.of(comment));

		// When
		commentService.updateComment(boardId, commentId, commentSaveRequest, requestUserId);

		// Then
		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verify(userRepository, times(1)).findById(requestUserId);

		assertThat(comment.getComment()).isEqualTo(commentSaveRequest.getComment());

	}

	@Test
	@DisplayName("댓글 수정 - 댓글 없음")
	void updateCommentNoComment() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();

		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenThrow(
			new CustomException(ErrorCode.COMMENT_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> commentService.updateComment(boardId, commentId, commentSaveRequest, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMMENT_NOT_FOUND);

		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("댓글 수정 - 유저 없음")
	void updateCommentNoUser() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();

		User user = createUser(requestUserId);
		Board board = createBoard(boardId);
		Comment comment = createComment(commentId, board, user);

		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenReturn(Optional.of(comment));
		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> commentService.updateComment(boardId, commentId, commentSaveRequest, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verify(userRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("댓글 수정 - 작성자 불일치")
	void updateCommentNotAuthor() {
		// given
		CommentSaveRequest commentSaveRequest = createSaveRequest();

		User requestUser = createUser(requestUserId);
		User authorUser = createUser(authorUserId);
		Board board = createBoard(boardId);
		Comment comment = createComment(commentId, board, authorUser);

		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenReturn(Optional.of(comment));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(requestUser));

		// When & Then
		assertThatThrownBy(() -> commentService.updateComment(boardId, commentId, commentSaveRequest, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BAD_REQUEST);

		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verify(userRepository, times(1)).findById(requestUserId);
	}

	@Test
	@DisplayName("댓글 삭제 - 성공")
	void deleteCommentSuccess() {
		// given
		User user = createUser(requestUserId);
		Board board = createBoard(boardId);
		Comment comment = createComment(commentId, board, user);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenReturn(Optional.of(comment));

		// When
		commentService.deleteComment(boardId, commentId, requestUserId);

		// Then
		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verify(userRepository, times(1)).findById(requestUserId);
		verify(commentRepository, times(1)).delete(comment);
	}

	@Test
	@DisplayName("댓글 삭제 - 댓글 없음")
	void deleteCommentNoComment() {
		// given
		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenThrow(
			new CustomException(ErrorCode.COMMENT_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> commentService.deleteComment(boardId, commentId, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMMENT_NOT_FOUND);

		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("댓글 삭제 - 유저 없음")
	void deleteCommentNoUser() {
		// given
		User user = createUser(requestUserId);
		Board board = createBoard(boardId);
		Comment comment = createComment(commentId, board, user);

		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenReturn(Optional.of(comment));
		when(userRepository.findById(anyLong())).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThatThrownBy(() -> commentService.deleteComment(boardId, commentId, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);

		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verify(userRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("댓글 삭제 - 작성자 불일치")
	void deleteCommentNotAuthor() {
		// given
		User requestUser = createUser(requestUserId);
		User authorUser = createUser(authorUserId);
		Board board = createBoard(boardId);
		Comment comment = createComment(commentId, board, authorUser);

		when(commentRepository.findByCommentIdAndBoard_BoardId(anyLong(), anyLong())).thenReturn(Optional.of(comment));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(requestUser));

		// When & Then
		assertThatThrownBy(() -> commentService.deleteComment(boardId, commentId, requestUserId))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BAD_REQUEST);

		verify(commentRepository, times(1)).findByCommentIdAndBoard_BoardId(commentId, boardId);
		verify(userRepository, times(1)).findById(requestUserId);
	}
}