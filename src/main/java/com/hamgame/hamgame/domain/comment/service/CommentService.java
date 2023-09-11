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
		User user = userRepository.getReferenceById(userId);
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		Comment comment = commentSaveRequest.toEntity(board, user);
		commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(Long boardId, Long commentId, CommentSaveRequest commentSaveRequest, Long userId) {
		Comment comment = commentRepository.findByCommentIdAndBoardId(commentId, boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

		isAuthorOfComment(comment, userId);

		comment.updateComment(commentSaveRequest.getComment());
	}

	@Transactional
	public void deleteComment(Long boardId, Long commentId, Long userId) {
		Comment comment = commentRepository.findByCommentIdAndBoardId(commentId, boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

		isAuthorOfComment(comment, userId);

		commentRepository.delete(comment);
	}

	public void isAuthorOfComment(Comment comment, Long userId) {
		if (!Objects.equals(comment.getUser().getId(), userId)) {
			throw new CustomException(ErrorCode.NOT_COMMENT_AUTHOR);
		}
	}
}
