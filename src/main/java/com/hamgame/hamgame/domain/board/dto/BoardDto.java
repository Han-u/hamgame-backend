package com.hamgame.hamgame.domain.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.comment.dto.CommentDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 단건 조회 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
	@Schema(description = "게시글 번호")
	private Long boardId;
	@Schema(description = "제목")
	private String title;
	@Schema(description = "내용")
	private String content;
	@Schema(description = "조회수")
	private int viewCount;
	@Schema(description = "카테고리")
	private BoardCategory boardCategory;
	@Schema(description = "첨부 이미지 url")
	private String imageUrl;
	@Schema(description = "댓글 수")
	private int commentCount;
	@Schema(description = "작성일자")
	private LocalDateTime createdAt;
	@Schema(description = "댓글")
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
