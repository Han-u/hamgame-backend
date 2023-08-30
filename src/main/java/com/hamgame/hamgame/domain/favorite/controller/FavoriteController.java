package com.hamgame.hamgame.domain.favorite.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.domain.favorite.dto.FavAddRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavRemoveRequest;
import com.hamgame.hamgame.domain.favorite.dto.FavUpdateRequest;
import com.hamgame.hamgame.domain.favorite.service.FavoriteService;
import com.hamgame.hamgame.domain.game.dto.GameDto;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Api(tags = "즐겨찾는 게임")
@RequiredArgsConstructor
@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	@Operation(summary = "즐겨찾는 게임 조회", description = "내가 즐겨찾기를 한 게임들을 조회합니다.")
	@GetMapping
	public List<GameDto> getFavoriteGameList(@CurrentUser UserPrincipal userPrincipal) {
		return favoriteService.getFavoriteGameList(userPrincipal.getId());
	}

	@Operation(summary = "즐겨찾는 게임 추가", description = "내 즐겨찾기 목록에 게임을 추가합니다.")
	@PostMapping
	public void addFavoriteGames(@RequestBody @Valid FavAddRequest requestDto,
		@CurrentUser UserPrincipal userPrincipal) {
		favoriteService.addGame(requestDto, userPrincipal.getId());
	}

	@Operation(summary = "즐겨찾는 게임 수정", description = "기존 즐겨찾기 목록을 삭제하고 게임을 수정합니다.")
	@PutMapping
	public void updateFavoriteGames(@RequestBody @Valid FavUpdateRequest requestDto,
		@CurrentUser UserPrincipal userPrincipal) {
		favoriteService.updateGames(requestDto, userPrincipal.getId());
	}

	@Operation(summary = "즐겨찾는 게임 삭제", description = "특정 게임 즐겨찾기를 해제합니다.")
	@DeleteMapping
	public void removeGames(@RequestBody @Valid FavRemoveRequest requestDto, @CurrentUser UserPrincipal userPrincipal) {
		favoriteService.removeGame(requestDto, userPrincipal.getId());
	}

}
