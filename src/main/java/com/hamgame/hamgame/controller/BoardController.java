package com.hamgame.hamgame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.Board;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/games/{gameId}/boards")
@RestController
public class BoardController {

	@GetMapping("")
	public List<Board> getBoardList(@PathVariable int gameId) {
		return null;
	}

	@GetMapping("/{boardId}")
	public Board getBoard(@PathVariable int gameId, @PathVariable int boardId) {
		return null;
	}

	@PostMapping("")
	public void createBoard(@PathVariable int gameId) {
	}

	@PutMapping("/{boardId}")
	public void updateBoard(@PathVariable int gameId, @PathVariable int boardId) {
	}

	@DeleteMapping("/{boardId}")
	public void deleteBoard(@PathVariable int gameId, @PathVariable int boardId) {
	}

}
