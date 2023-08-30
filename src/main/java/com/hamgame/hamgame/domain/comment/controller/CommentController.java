package com.hamgame.hamgame.domain.comment.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.domain.comment.dto.CommentSaveRequest;
import com.hamgame.hamgame.domain.comment.service.CommentService;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@Api(tags = "댓글")
@RestController
@RequestMapping("/games/{gameId}/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
	@PostMapping
	public void createComment(
		@Parameter(description = "게임 id") @PathVariable Long gameId,
		@Parameter(description = "게시글 id") @PathVariable Long boardId,
		@RequestBody @Valid CommentSaveRequest commentSaveRequest,
		@CurrentUser UserPrincipal userPrincipal) {
		commentService.createComment(boardId, commentSaveRequest, userPrincipal.getId());

	}

	@Operation(summary = "댓글 수정", description = "나의 특정 댓글을 수정합니다.")
	@PutMapping("/{commentId}")
	public void updateComment(
		@Parameter(description = "게임 id") @PathVariable Long gameId,
		@Parameter(description = "게시글 id") @PathVariable Long boardId,
		@Parameter(description = "댓글 id") @PathVariable Long commentId,
		@RequestBody @Valid CommentSaveRequest commentSaveRequest,
		@CurrentUser UserPrincipal userPrincipal) {
		commentService.updateComment(boardId, commentId, commentSaveRequest, userPrincipal.getId());
	}

	@Operation(summary = "댓글 삭제", description = "나의 특정 댓글을 삭제합니다.")
	@DeleteMapping("/{commentId}")
	public void deleteComment(
		@Parameter(description = "게임 id") @PathVariable Long gameId,
		@Parameter(description = "게시글 id") @PathVariable Long boardId,
		@Parameter(description = "댓글 id") @PathVariable Long commentId,
		@CurrentUser UserPrincipal userPrincipal) {
		commentService.deleteComment(boardId, commentId, userPrincipal.getId());
	}
}
