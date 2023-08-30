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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/games/{gameId}/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public void createComment(@PathVariable Long gameId, @PathVariable Long boardId,
		@RequestBody @Valid CommentSaveRequest commentSaveRequest, @CurrentUser UserPrincipal userPrincipal) {
		commentService.createComment(boardId, commentSaveRequest, userPrincipal.getId());

	}

	@PutMapping("/{commentId}")
	public void updateComment(@PathVariable Long gameId, @PathVariable Long boardId, @PathVariable Long commentId,
		@RequestBody @Valid CommentSaveRequest commentSaveRequest, @CurrentUser UserPrincipal userPrincipal) {
		commentService.updateComment(boardId, commentId, commentSaveRequest, userPrincipal.getId());
	}

	@DeleteMapping("/{commentId}")
	public void deleteComment(@PathVariable Long gameId, @PathVariable Long boardId, @PathVariable Long commentId,
		@CurrentUser UserPrincipal userPrincipal) {
		commentService.deleteComment(boardId, commentId, userPrincipal.getId());
	}
}
