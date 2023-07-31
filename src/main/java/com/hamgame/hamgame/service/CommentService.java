package com.hamgame.hamgame.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.Comment;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.dto.CommentSaveReq;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.ErrorCode;
import com.hamgame.hamgame.repository.BoardRepository;
import com.hamgame.hamgame.repository.CommentRepository;
import com.hamgame.hamgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;

	private final UserRepository userRepository;

	public void createComment(Long boardId, CommentSaveReq commentSaveReq, UserPrincipal userPrincipal) {
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		Comment comment = commentSaveReq.toEntity(board, user);
		commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(Long boardId, Long commentId, CommentSaveReq commentSaveReq,
		UserPrincipal userPrincipal) {
		Comment comment = commentRepository.findByCommentIdAndBoard_BoardId(commentId, boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		isAuthorOfComment(comment, user);
		comment.updateComment(commentSaveReq.getComment());
	}

	public void deleteComment(Long boardId, Long commentId, UserPrincipal userPrincipal) {
		Comment comment = commentRepository.findByCommentIdAndBoard_BoardId(commentId, boardId)
			.orElseThrow(RuntimeException::new);
		User user = userRepository.findById(userPrincipal.getId())
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
