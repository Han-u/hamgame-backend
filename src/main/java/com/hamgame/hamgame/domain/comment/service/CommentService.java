package com.hamgame.hamgame.domain.comment.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.repository.BoardRepository;
import com.hamgame.hamgame.domain.comment.dto.CommentSaveRequest;
import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.comment.entity.repository.CommentRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;

	private final UserRepository userRepository;

	@Transactional
	public void createComment(Long boardId, CommentSaveRequest commentSaveRequest, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		Comment comment = commentSaveRequest.toEntity(board, user);
		commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(Long boardId, Long commentId, CommentSaveRequest commentSaveRequest,
		Long userId) {
		Comment comment = commentRepository.findByCommentIdAndBoard_BoardId(commentId, boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		isAuthorOfComment(comment, user);
		comment.updateComment(commentSaveRequest.getComment());
	}

	@Transactional
	public void deleteComment(Long boardId, Long commentId, Long userId) {
		Comment comment = commentRepository.findByCommentIdAndBoard_BoardId(commentId, boardId)
			.orElseThrow(RuntimeException::new);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		isAuthorOfComment(comment, user);
		commentRepository.delete(comment);
	}

	public void isAuthorOfComment(Comment comment, User user) {
		if (!Objects.equals(comment.getUser().getId(), user.getId())) {
			throw new CustomException(ErrorCode.BAD_REQUEST);
		}
	}
}
