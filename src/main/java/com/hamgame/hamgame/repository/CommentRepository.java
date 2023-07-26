package com.hamgame.hamgame.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Optional<Comment> findByCommentIdAndBoard_BoardId(Long commentId, Long boardId);
}
