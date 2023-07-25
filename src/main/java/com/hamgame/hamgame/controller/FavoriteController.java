package com.hamgame.hamgame.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.dto.AddFavGamesReq;
import com.hamgame.hamgame.dto.RemoveFavGamesReq;
import com.hamgame.hamgame.dto.UpdateFavGamesReq;
import com.hamgame.hamgame.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	@GetMapping
	public Set<Game> getFavoriteGameList() {
		return favoriteService.getFavoriteGameList();
	}

	@PostMapping
	public void addFavoriteGames(AddFavGamesReq requestDto) {
		favoriteService.addGame(requestDto);
	}

	@PutMapping
	public void updateFavoriteGames(UpdateFavGamesReq requestDto) {
		favoriteService.updateGames(requestDto);
	}

	@DeleteMapping
	public void removeGames(RemoveFavGamesReq requestDto) {
		favoriteService.removeGame(requestDto);
	}

}
