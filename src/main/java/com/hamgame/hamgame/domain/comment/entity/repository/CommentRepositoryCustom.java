package com.hamgame.hamgame.domain.comment.entity.repository;

import java.util.Optional;

import com.hamgame.hamgame.domain.comment.entity.Comment;

public interface CommentRepositoryCustom {
	Optional<Comment> findByCommentIdAndBoardId(Long commentId, Long boardId);
}
