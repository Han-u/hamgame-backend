package com.hamgame.hamgame.domain.favorite.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "즐겨찾는 게임 추가 요청 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavAddRequest {
	@Schema(description = "즐겨찾기 추가할 게임 ID 목록")
	@NotNull
	@Size(min = 1)
	private List<@NotNull Long> gameIds;
}
