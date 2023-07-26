package com.hamgame.hamgame.dto;

import java.time.LocalDateTime;

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
public class BoardListDto {
	private Long boardId;
	private String title;
	private String content;
	private String image;
	private int viewCount;
	private BoardCategory boardCategory;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String nickname;
	private int commentCount;

	public static BoardListDto of(Board board) {
		return BoardListDto.builder()
			.boardId(board.getBoardId())
			.title(board.getTitle())
			.content(board.getContent())
			.image(board.getImage())
			.viewCount(board.getViewCount())
			.boardCategory(board.getBoardCategory())
			.createdAt(board.getCreatedAt())
			.updatedAt(board.getUpdatedAt())
			.nickname(board.getUser().getNickname())
			.commentCount(board.getComment().size())
			.build();
	}
}
