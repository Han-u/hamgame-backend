package com.hamgame.hamgame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.dto.AddFavGamesReq;
import com.hamgame.hamgame.dto.GameDto;
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
	public List<GameDto> getFavoriteGameList(@CurrentUser UserPrincipal userPrincipal) {
		return favoriteService.getFavoriteGameList(userPrincipal);
	}

	@PostMapping
	public void addFavoriteGames(AddFavGamesReq requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.addGame(requestDto, userPrincipal);
	}

	@PutMapping
	public void updateFavoriteGames(UpdateFavGamesReq requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.updateGames(requestDto, userPrincipal);
	}

	@DeleteMapping
	public void removeGames(RemoveFavGamesReq requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.removeGame(requestDto, userPrincipal);
	}

}
