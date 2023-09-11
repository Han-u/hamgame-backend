package com.hamgame.hamgame.domain.comment.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
	Optional<Comment> findByCommentIdAndBoard_BoardId(Long commentId, Long boardId);
}
