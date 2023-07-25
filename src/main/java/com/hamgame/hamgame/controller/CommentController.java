package com.hamgame.hamgame.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/games/{gameId}/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

	@PostMapping("")
	public void createComment(@PathVariable int gameId, @PathVariable int boardId) {
	}

	@PutMapping("/{commentId}")
	public void updateComment(@PathVariable int gameId, @PathVariable int boardId, @PathVariable int commentId) {
	}

	@DeleteMapping("/{commentId}")
	public void deleteComment(@PathVariable int gameId, @PathVariable int boardId, @PathVariable int commentId) {
	}
}
