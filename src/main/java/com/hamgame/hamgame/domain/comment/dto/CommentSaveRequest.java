package com.hamgame.hamgame.domain.comment.dto;

import javax.validation.constraints.NotBlank;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 작성/수정 요청DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveRequest {
	@Schema(description = "댓글 내용")
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
