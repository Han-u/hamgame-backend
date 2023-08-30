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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/games/{gameId}/boards")
@RestController
public class BoardController {

	private final BoardService boardService;

	@GetMapping
	public Page<BoardListDto> getBoardList(@PathVariable Long gameId,
		@PageableDefault(sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(value = "category", required = false) BoardCategory boardCategory) {
		return boardService.getBoardList(gameId, pageable, boardCategory);
	}

	@GetMapping("/{boardId}")
	public BoardDto getBoard(@PathVariable Long gameId, @PathVariable Long boardId) {
		return boardService.getBoard(gameId, boardId);
	}

	@PostMapping
	public void createBoard(@PathVariable Long gameId, @RequestBody @Valid BoardSaveRequest boardSaveRequest,
		@CurrentUser
		UserPrincipal userPrincipal) {
		boardService.createBoard(gameId, boardSaveRequest, userPrincipal.getId());
	}

	@PutMapping("/{boardId}")
	public void updateBoard(@PathVariable Long gameId, @PathVariable Long boardId,
		@RequestBody @Valid BoardSaveRequest boardSaveRequest, @CurrentUser UserPrincipal userPrincipal) {
		boardService.updateBoard(gameId, boardId, boardSaveRequest, userPrincipal.getId());
	}

	@DeleteMapping("/{boardId}")
	public void deleteBoard(@PathVariable Long gameId, @PathVariable Long boardId,
		@CurrentUser UserPrincipal userPrincipal) {
		boardService.deleteBoard(gameId, boardId, userPrincipal.getId());
	}

}
