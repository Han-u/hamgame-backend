package com.hamgame.hamgame.domain.crawler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.hamgame.hamgame.domain.crawler.util.DateFormatter;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;

public abstract class Crawler<R, E, I> {
	protected GameDto game;
	protected String url;
	protected String timeFormat;
	protected String dateFormat;
	protected boolean isGmt;

	public Crawler(GameNoticeConfigDto dto) {
		this.game = dto.getGame();
		this.url = dto.getUrl();
		this.timeFormat = dto.getTimeFormat();
		this.dateFormat = dto.getDateFormat();
		this.isGmt = dto.isGmt();
	}

	public abstract I getNoticeList(R object);

	public abstract String getNoticeUrl(E object);

	public abstract String getNoticeType(E object);

	public abstract String getTitle(E object);

	public abstract String getTime(E object);

	public abstract String getImageUrl(E object);

	public boolean isNotBetweenDay(E object, LocalDateTime start, LocalDateTime end) {
		LocalDateTime target = getCreateAt(object);
		return !DateFormatter.isBetweenDay(target, start, end);
	}

	public LocalDateTime getCreateAt(E object) {
		String time = getTime(object);
		LocalDateTime createAt = LocalDateTime.now();
		if (timeFormat != null && DateFormatter.isValidTimeFormat(time, timeFormat)) {
			createAt = DateFormatter.getTime(time, timeFormat);
		} else if (DateFormatter.isValidDateFormat(time, dateFormat)) {
			createAt = DateFormatter.getDate(time, dateFormat);
		}

		if (isGmt) {
			createAt = DateFormatter.gmtToKst(createAt);
		}
		return createAt;
	}

	public abstract R process() throws IOException;

	public abstract List<GameNoticeDto> run(LocalDateTime start, LocalDateTime end);

	public GameNoticeDto toDto(E object) {
		String noticeUrl = getNoticeUrl(object);
		String noticeType = getNoticeType(object);
		String title = getTitle(object);
		String imageUrl = getImageUrl(object);
		LocalDateTime createAt = getCreateAt(object);

		return GameNoticeDto.builder()
			.title(title)
			.noticeType(noticeType)
			.noticeUrl(noticeUrl)
			.imageUrl(imageUrl)
			.game(game)
			.postCreatedAt(createAt)
			.build();
	}
}
