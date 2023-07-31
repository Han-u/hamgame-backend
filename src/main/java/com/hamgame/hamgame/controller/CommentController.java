package com.hamgame.hamgame.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.dto.CommentSaveReq;
import com.hamgame.hamgame.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/games/{gameId}/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public void createComment(@PathVariable Long gameId, @PathVariable Long boardId,
		@RequestBody @Valid CommentSaveReq commentSaveReq, @CurrentUser UserPrincipal userPrincipal) {
		commentService.createComment(boardId, commentSaveReq, userPrincipal);

	}

	@PutMapping("/{commentId}")
	public void updateComment(@PathVariable Long gameId, @PathVariable Long boardId, @PathVariable Long commentId,
		@RequestBody @Valid CommentSaveReq commentSaveReq, @CurrentUser UserPrincipal userPrincipal) {
		commentService.updateComment(boardId, commentId, commentSaveReq, userPrincipal);
	}

	@DeleteMapping("/{commentId}")
	public void deleteComment(@PathVariable Long gameId, @PathVariable Long boardId, @PathVariable Long commentId,
		@CurrentUser UserPrincipal userPrincipal) {
		commentService.deleteComment(boardId, commentId, userPrincipal);
	}
}
