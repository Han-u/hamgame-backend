package com.hamgame.hamgame.domain.game.dto;

import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.GameCategory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GameDto {

	private Long gameId;

	private String name;

	private GameCategory category;

	private String imageUrl;

	private String homepageUrl;

	public static GameDto of(Game game) {
		return GameDto.builder()
			.gameId(game.getGameId())
			.name(game.getName())
			.category(game.getCategory())
			.imageUrl(game.getImageUrl())
			.homepageUrl(game.getHomepageUrl())
			.build();
	}

	public Game toEntity() {
		return Game.builder()
			.gameId(gameId)
			.name(name)
			.category(category)
			.imageUrl(imageUrl)
			.homepageUrl(homepageUrl)
			.build();
	}
}
