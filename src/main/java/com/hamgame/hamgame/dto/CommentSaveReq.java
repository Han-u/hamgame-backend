package com.hamgame.hamgame.dto;

import javax.validation.constraints.NotBlank;

import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.Comment;
import com.hamgame.hamgame.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveReq {
	@NotBlank
	private String comment;

	public Comment toEntity(Board board, User user) {
		return Comment.builder()
			.comment(comment)
			.board(board)
			.user(user)
			.build();
	}
}
