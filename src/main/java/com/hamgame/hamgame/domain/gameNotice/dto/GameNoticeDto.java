package com.hamgame.hamgame.domain.gameNotice.dto;

import java.time.LocalDateTime;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNotice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameNoticeDto {
	private Long gameNoticeId;
	private String title;
	private String noticeType;
	private String noticeUrl;
	private String imageUrl;
	private LocalDateTime postCreatedAt;
	private GameDto game;

	public static GameNoticeDto of(GameNotice gameNotice) {
		return GameNoticeDto.builder()
			.gameNoticeId(gameNotice.getGameNoticeId())
			.title(gameNotice.getTitle())
			.noticeType(gameNotice.getNoticeType())
			.noticeUrl(gameNotice.getNoticeUrl())
			.imageUrl(gameNotice.getImageUrl())
			.postCreatedAt(gameNotice.getPostCreatedAt())
			.game(GameDto.of(gameNotice.getGame()))
			.build();
	}

	public GameNotice toEntity() {
		return GameNotice.builder()
			.title(title)
			.noticeType(noticeType)
			.noticeUrl(noticeUrl)
			.game(game.toEntity())
			.imageUrl(imageUrl)
			.postCreatedAt(postCreatedAt)
			.build();
	}
}
