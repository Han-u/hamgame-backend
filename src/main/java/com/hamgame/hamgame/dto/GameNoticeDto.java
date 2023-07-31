package com.hamgame.hamgame.dto;

import com.hamgame.hamgame.domain.GameNotice;
import com.hamgame.hamgame.domain.type.NoticeType;

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
	private NoticeType noticeType;
	private String url;
	private GameDto game;

	public static GameNoticeDto of(GameNotice gameNotice) {
		return GameNoticeDto.builder()
			.gameNoticeId(gameNotice.getGameNoticeId())
			.title(gameNotice.getTitle())
			.noticeType(gameNotice.getNoticeType())
			.url(gameNotice.getUrl())
			.game(GameDto.of(gameNotice.getGame()))
			.build();
	}
}
