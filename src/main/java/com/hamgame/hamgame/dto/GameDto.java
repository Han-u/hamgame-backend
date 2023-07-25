package com.hamgame.hamgame.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.type.GameCategory;

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

	public static List<GameDto> of(List<Game> games) {
		return games.stream().map(GameDto::of).collect(Collectors.toList());
	}
}
