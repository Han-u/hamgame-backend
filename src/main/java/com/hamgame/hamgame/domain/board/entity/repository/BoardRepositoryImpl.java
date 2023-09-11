package com.hamgame.hamgame.domain.board.entity.repository;

import static com.hamgame.hamgame.domain.board.entity.QBoard.*;
import static com.hamgame.hamgame.domain.comment.entity.QComment.*;
import static com.hamgame.hamgame.domain.user.entity.QUser.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.hamgame.hamgame.domain.board.dto.BoardListDto;
import com.hamgame.hamgame.domain.board.dto.QBoardListDto;
import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.board.entity.QBoard;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	public QBoardListDto createBoardListDto() {
		return new QBoardListDto(board.boardId, board.title, board.content, board.image, board.viewCount,
			board.boardCategory, board.createdAt,
			board.updatedAt, board.user.nickname, board.comment.size());
	}

	@Override
	public Optional<Board> findByBoardIdAndGame_GameId(Long boardId, Long gameId) {
		QBoard qBoard = board;
		return Optional.ofNullable(jpaQueryFactory.selectFrom(qBoard)
			.leftJoin(qBoard.comment, comment1).fetchJoin()
			.leftJoin(comment1.user).fetchJoin()
			.where(qBoard.game.gameId.eq(gameId))
			.where(qBoard.boardId.eq(boardId))
			.fetchOne());
	}

	@Override
	public Optional<Board> findByBoardId(Long boardId) {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(board)
			.leftJoin(board.comment, comment1).fetchJoin()
			.leftJoin(comment1.user, user).fetchJoin()
			.where(board.boardId.eq(boardId))
			.fetchOne());
	}

	@Override
	public Page<BoardListDto> getBoardListPage(Pageable pageable, Long gameId, BoardCategory category) {
		List<BoardListDto> boardList = jpaQueryFactory.select(createBoardListDto())
			.from(board)
			.innerJoin(board.game)
			.innerJoin(board.user)
			.where(board.game.gameId.eq(gameId), categoryEq(category))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(new OrderSpecifier<>(Order.DESC, board.createdAt))
			.fetch();

		Long count = jpaQueryFactory.select(board.count())
			.from(board)
			.where(board.game.gameId.eq(gameId), categoryEq(category))
			.fetchOne();
		return new PageImpl<>(boardList, pageable, count);
	}

	private BooleanExpression categoryEq(BoardCategory category) {
		return category == null ? null : board.boardCategory.eq(category);
	}
}
