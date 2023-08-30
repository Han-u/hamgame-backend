package com.hamgame.hamgame.domain.gameNotice.dto;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNoticeConfig;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게임 공지 스크랩 설정 정보")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameNoticeConfigDto {
	@Schema(description = "게임")
	private GameDto game;
	@Schema(description = "공지 페이지 url")
	private String url;
	@Schema(description = "날짜 포맷")
	private String dateFormat;
	@Schema(description = "시간 포맷")
	private String timeFormat;
	@Schema(description = "시간대(GMT)")
	private boolean isGmt;
	@Schema(description = "대상 크롤러")
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
