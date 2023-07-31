package com.hamgame.hamgame.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hamgame.hamgame.domain.Board;
import com.hamgame.hamgame.domain.type.BoardCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
	private Long boardId;
	private String title;
	private String content;
	private int viewCount;
	private BoardCategory boardCategory;
	private String imageUrl;
	private int commentCount;
	private LocalDateTime createdAt;
	private List<CommentDto> comments = new ArrayList<>();

	public static BoardDto of(Board board) {
		return BoardDto.builder()
			.boardId(board.getBoardId())
			.title(board.getTitle())
			.content(board.getContent())
			.viewCount(board.getViewCount())
			.boardCategory(board.getBoardCategory())
			.imageUrl(board.getImage())
			.commentCount(board.getComment().size())
			.createdAt(board.getCreatedAt())
			.comments(CommentDto.of(board.getComment()))
			.build();
	}
}
