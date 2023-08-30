package com.hamgame.hamgame.domain.gameNotice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.service.GameNoticeService;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Api(tags = "게임 공지")
@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class GameNoticeController {

	private final GameNoticeService gameNoticeService;

	@Operation(summary = "내 게임 공지 조회", description = "내가 즐겨찾기 한 게임의 공지 목록을 조회합니다.")
	@GetMapping
	public Page<GameNoticeDto> getMyGameNoticeList(
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@CurrentUser UserPrincipal userPrincipal) {
		return gameNoticeService.getMyGameNoticeList(pageable, userPrincipal.getId());
	}
}
