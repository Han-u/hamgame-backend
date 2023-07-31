package com.hamgame.hamgame.domain.gameNotice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.common.CurrentUser;
import com.hamgame.hamgame.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.service.GameNoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class GameNoticeController {

	private final GameNoticeService gameNoticeService;

	@GetMapping
	public Page<GameNoticeDto> getMyGameNoticeList(
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @CurrentUser
	UserPrincipal userPrincipal) {
		return gameNoticeService.getMyGameNoticeList(pageable, userPrincipal);
	}
}
