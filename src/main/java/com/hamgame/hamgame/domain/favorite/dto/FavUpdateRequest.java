package com.hamgame.hamgame.domain.favorite.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "즐겨찾기 수정할 게임 요청DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FavUpdateRequest {
	@Schema(description = "즐겨찾기로 지정할 게임 ID 목록")
	@NotNull
	@Size(min = 1)
	private List<@NotNull Long> gameIds;
}