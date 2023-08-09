package com.hamgame.hamgame.domain.gameNotice.dto;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNoticeConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameNoticeConfigDto {
	private GameDto game;
	private String url;
	private String dateFormat;
	private String timeFormat;
	private boolean isGmt;
	private String crawlerName;

	public static GameNoticeConfigDto of(GameNoticeConfig entity) {
		return GameNoticeConfigDto.builder()
			.game(GameDto.of(entity.getGame()))
			.url(entity.getUrl())
			.dateFormat(entity.getDateFormat())
			.timeFormat(entity.getTimeFormat())
			.isGmt(entity.isGmt())
			.crawlerName(entity.getCrawlerName())
			.build();
	}
}
