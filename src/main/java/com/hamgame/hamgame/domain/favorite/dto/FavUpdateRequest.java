package com.hamgame.hamgame.domain.favorite.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FavUpdateRequest {
	@NotNull
	@Size(min = 1)
	private List<@NotNull Long> gameIds;
}