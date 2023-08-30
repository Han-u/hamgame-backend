package com.hamgame.hamgame.domain.board.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.domain.board.dto.BoardDto;
import com.hamgame.hamgame.domain.board.dto.BoardListDto;
import com.hamgame.hamgame.domain.board.dto.BoardSaveRequest;
import com.hamgame.hamgame.domain.board.entity.BoardCategory;
import com.hamgame.hamgame.domain.board.service.BoardService;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@Api(tags = "게시판")
@RequiredArgsConstructor
@RequestMapping("/games/{gameId}/boards")
@RestController
public class BoardController {

	private final BoardService boardService;

	@Operation(summary = "게시글 리스트 조회", description = "선택된 게임의 게시글을 조회합니다.")
	@GetMapping
	public Page<BoardListDto> getBoardList(
		@Parameter(description = "게임 Id") @PathVariable Long gameId,
		@PageableDefault(sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable,
		@Parameter(description = "게시글 카테고리") @RequestParam(value = "category", required = false) BoardCategory boardCategory) {
		return boardService.getBoardList(gameId, pageable, boardCategory);
	}

	@Operation(summary = "게시글 단건 조회", description = "특정 게시글을 조회합니다.")
	@GetMapping("/{boardId}")
	public BoardDto getBoard(
		@Parameter(description = "게임 Id") @PathVariable Long gameId,
		@Parameter(description = "게시글 Id") @PathVariable Long boardId) {
		return boardService.getBoard(gameId, boardId);
	}

	@Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
	@PostMapping
	public void createBoard(
		@Parameter(description = "게임 Id") @PathVariable Long gameId,
		@Parameter(description = "게시글 작성 정보") @RequestBody @Valid BoardSaveRequest boardSaveRequest,
		@Parameter(description = "사용자") @CurrentUser UserPrincipal userPrincipal) {
		boardService.createBoard(gameId, boardSaveRequest, userPrincipal.getId());
	}

	@Operation(summary = "게시글 수정", description = "나의 게시글을 수정합니다.")
	@PutMapping("/{boardId}")
	public void updateBoard(
		@Parameter(description = "게임 Id") @PathVariable Long gameId,
		@Parameter(description = "게시글 Id") @PathVariable Long boardId,
		@RequestBody @Valid BoardSaveRequest boardSaveRequest,
		@CurrentUser UserPrincipal userPrincipal) {
		boardService.updateBoard(gameId, boardId, boardSaveRequest, userPrincipal.getId());
	}

	@Operation(summary = "게시글 삭제", description = "나의 게시글을 삭제합니다.")
	@DeleteMapping("/{boardId}")
	public void deleteBoard(
		@Parameter(description = "게임 Id") @PathVariable Long gameId,
		@Parameter(description = "게시글 Id") @PathVariable Long boardId,
		@CurrentUser UserPrincipal userPrincipal) {
		boardService.deleteBoard(gameId, boardId, userPrincipal.getId());
	}

}
