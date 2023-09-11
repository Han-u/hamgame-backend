package com.hamgame.hamgame.domain.gameNotice.entity.repository;

import static com.hamgame.hamgame.domain.game.entity.QGame.*;
import static com.hamgame.hamgame.domain.gameNotice.entity.QGameNotice.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.hamgame.hamgame.domain.game.dto.QGameDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.dto.QGameNoticeDto;
import com.hamgame.hamgame.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameNoticeRepositoryImpl implements GameNoticeRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	public QGameDto createGameDto() {
		return new QGameDto(game.gameId, game.name, game.category,
			game.imageUrl, game.homepageUrl);
	}

	public QGameNoticeDto createGameNoticeDto() {
		return new QGameNoticeDto(
			gameNotice.gameNoticeId, gameNotice.title, gameNotice.noticeType,
			gameNotice.noticeUrl, gameNotice.imageUrl, gameNotice.postCreatedAt,
			createGameDto()
		);
	}

	@Override
	public Page<GameNoticeDto> findFavoriteNoticeByUserId(Long userId, Pageable pageable) {
		List<GameNoticeDto> notices = jpaQueryFactory.select(createGameNoticeDto())
			.from(QUser.user)
			.innerJoin(QUser.user.games, game)
			.innerJoin(gameNotice).on(gameNotice.game.eq(game))
			.where(QUser.user.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		Long count = jpaQueryFactory.select(gameNotice.count())
			.from(QUser.user)
			.innerJoin(QUser.user.games, game)
			.innerJoin(gameNotice).on(gameNotice.game.eq(game))
			.where(QUser.user.id.eq(userId))
			.fetchOne();
		return new PageImpl<>(notices, pageable, count);
	}
}
