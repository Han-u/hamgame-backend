package com.hamgame.hamgame.domain.gameNotice.dto;

import java.time.LocalDateTime;

import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNotice;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게임 공지 응답DTO")
@Getter
@NoArgsConstructor
@Builder
public class GameNoticeDto {
	@Schema(description = "공지 번호")
	private Long gameNoticeId;
	@Schema(description = "제목")
	private String title;
	@Schema(description = "카테고리")
	private String noticeType;
	@Schema(description = "공지 url")
	private String noticeUrl;
	@Schema(description = "이미지 url")
	private String imageUrl;
	@Schema(description = "공지 작성 일자")
	private LocalDateTime postCreatedAt;
	@Schema(description = "게임 정보")
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

	@QueryProjection
	public GameNoticeDto(Long gameNoticeId, String title, String noticeType, String noticeUrl, String imageUrl,
		LocalDateTime postCreatedAt, GameDto game) {
		this.gameNoticeId = gameNoticeId;
		this.title = title;
		this.noticeType = noticeType;
		this.noticeUrl = noticeUrl;
		this.imageUrl = imageUrl;
		this.postCreatedAt = postCreatedAt;
		this.game = game;
	}
}
