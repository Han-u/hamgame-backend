package com.hamgame.hamgame.domain.game.dto;

import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.game.entity.GameCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게임 조회 응답 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GameDto {
	@Schema(description = "게임 번호")
	private Long gameId;

	@Schema(description = "게임명")
	private String name;

	@Schema(description = "카테고리")
	private GameCategory category;

	@Schema(description = "대표 이미지")
	private String imageUrl;

	@Schema(description = "홈페이지 url")
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
