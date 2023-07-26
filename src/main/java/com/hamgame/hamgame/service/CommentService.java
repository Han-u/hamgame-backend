package com.hamgame.hamgame.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.Comment;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.dto.CommentSaveReq;
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

	public void createComment(Long boardId, CommentSaveReq commentSaveReq) {
		User user = userRepository.findById(1L).orElseThrow(RuntimeException::new); // 임시
		Board board = boardRepository.findById(boardId).orElseThrow(RuntimeException::new);
		Comment comment = commentSaveReq.toEntity(board, user);
		commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(Long boardId, Long commentId, CommentSaveReq commentSaveReq) {
		Comment comment = commentRepository.findByCommentIdAndBoard_BoardId(commentId, boardId)
			.orElseThrow(RuntimeException::new); // 나중에 수정
		// if 본인이면
		comment.updateComment(commentSaveReq.getComment());
	}

	public void deleteComment(Long boardId, Long commentId) {
		Comment comment = commentRepository.findByCommentIdAndBoard_BoardId(commentId, boardId)
			.orElseThrow(RuntimeException::new);
		commentRepository.delete(comment);
	}
}
