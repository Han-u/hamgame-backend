package com.hamgame.hamgame.domain.comment.dto;

import javax.validation.constraints.NotBlank;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {
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
