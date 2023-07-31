package com.hamgame.hamgame.domain.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveRequest {
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
