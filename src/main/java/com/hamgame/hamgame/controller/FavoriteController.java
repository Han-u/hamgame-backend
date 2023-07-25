package com.hamgame.hamgame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.Game;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	@GetMapping("")
	public List<Game> getFavoriteGameList() {
		return null;
	}

	@PostMapping("")
	public void addFavoriteGames() {
	}
}
