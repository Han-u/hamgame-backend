package com.hamgame.hamgame.domain.game.entity.repository;

import static com.hamgame.hamgame.domain.game.entity.QGame.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.game.dto.QGameDto;
import com.hamgame.hamgame.domain.game.entity.QGame;
import com.hamgame.hamgame.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class GameRepositoryImpl implements GameRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	public QGameDto createGameDto() {
		return new QGameDto(game.gameId, game.name, game.category,
			game.imageUrl, game.homepageUrl);
	}

	@Override
	public List<GameDto> findFavoriteGamesByUser(Long userId) {
		return jpaQueryFactory.select(createGameDto())
			.from(QUser.user)
			.innerJoin(QUser.user.games, QGame.game)
			.where(QUser.user.id.eq(userId))
			.fetch();
	}
}
