package com.hamgame.hamgame.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.domain.type.BoardCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveReq {
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	private String image;
	@NotNull
	private BoardCategory boardCategory;

	public Board toEntity(Game game, User user) {
		return Board.builder()
			.title(title)
			.content(content)
			.image(image)
			.game(game)
			.user(user)
			.boardCategory(boardCategory)
			.build();
	}
}
