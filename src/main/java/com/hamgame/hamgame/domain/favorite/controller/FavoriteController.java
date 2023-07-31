package com.hamgame.hamgame.domain.favorite.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.domain.favorite.dto.FavAddRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavRemoveRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavUpdateRequest;
import com.hamgame.hamgame.domain.favorite.service.FavoriteService;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.security.auth.UserPrincipal;

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
	public void addFavoriteGames(FavAddRequest requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.addGame(requestDto, userPrincipal);
	}

	@PutMapping
	public void updateFavoriteGames(FavUpdateRequest requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.updateGames(requestDto, userPrincipal);
	}

	@DeleteMapping
	public void removeGames(FavRemoveRequest requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.removeGame(requestDto, userPrincipal);
	}

}
