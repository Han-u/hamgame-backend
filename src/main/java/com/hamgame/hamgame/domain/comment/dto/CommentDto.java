package com.hamgame.hamgame.domain.comment.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.user.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 조회 응답DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	@Schema(description = "댓글 번호")
	private Long commentId;
	@Schema(description = "댓글 내용")
	private String comment;
	@Schema(description = "작성자")
	private UserDto userDto;

	public static CommentDto of(Comment comment) {
		return CommentDto.builder()
			.commentId(comment.getCommentId())
			.comment(comment.getComment())
			.userDto(UserDto.of(comment.getUser()))
			.build();
	}

	public static List<CommentDto> of(List<Comment> commentList) {
		return commentList.stream().map(CommentDto::of).collect(Collectors.toList());
	}
}
