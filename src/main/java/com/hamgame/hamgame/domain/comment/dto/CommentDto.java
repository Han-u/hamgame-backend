package com.hamgame.hamgame.domain.comment.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.hamgame.hamgame.domain.comment.entity.Comment;
import com.hamgame.hamgame.domain.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	private Long commentId;
	private String comment;
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
