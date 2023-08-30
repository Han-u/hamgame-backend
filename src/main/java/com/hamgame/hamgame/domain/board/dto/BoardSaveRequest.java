package com.hamgame.hamgame.domain.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 작성/수정 요청DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveRequest {
	@Schema(description = "제목")
	@NotBlank
	private String title;
	@Schema(description = "내용")
	@NotBlank
	private String content;
	@Schema(description = "첨부 이미지 url")
	private String image;
	@Schema(description = "카테고리")
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
