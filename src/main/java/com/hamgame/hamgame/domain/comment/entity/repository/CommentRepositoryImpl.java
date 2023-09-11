package com.hamgame.hamgame.domain.comment.entity.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Comment> findByCommentIdAndBoardId(Long commentId, Long boardId) {
		QComment qComment = QComment.comment1;
		return Optional.ofNullable(jpaQueryFactory.selectFrom(qComment)
			.where(qComment.commentId.eq(commentId))
			.where(qComment.board.boardId.eq(boardId))
			.fetchOne());
	}
}
