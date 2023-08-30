package com.hamgame.hamgame.domain.favorite.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "즐겨찾는 게임 삭제 요청 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FavRemoveRequest {
	@Schema(description = "즐겨찾기 삭제할 게임 ID")
	@NotNull
	private Long gameId;
}
