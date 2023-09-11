package com.hamgame.hamgame.domain.board.dto;

import java.time.LocalDateTime;

import com.hamgame.hamgame.domain.board.entity.Board;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시판 조회 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
public class BoardListDto {
	@Schema(description = "게시글 번호")
	private Long boardId;
	@Schema(description = "제목")
	private String title;
	@Schema(description = "내용")
	private String content;
	@Schema(description = "첨부 이미지 url")
	private String image;
	@Schema(description = "조회수")
	private int viewCount;
	@Schema(description = "카테고리")
	private BoardCategory boardCategory;
	@Schema(description = "작성일자")
	private LocalDateTime createdAt;
	@Schema(description = "수정일자")
	private LocalDateTime updatedAt;
	@Schema(description = "작성자")
	private String nickname;
	@Schema(description = "댓글 수")
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

	@QueryProjection
	public BoardListDto(Long boardId, String title, String content, String image, int viewCount,
		BoardCategory category, LocalDateTime createdAt, LocalDateTime updatedAt, String nickname, int commentCount) {
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.image = image;
		this.viewCount = viewCount;
		this.boardCategory = category;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.nickname = nickname;
		this.commentCount = commentCount;

	}
}
