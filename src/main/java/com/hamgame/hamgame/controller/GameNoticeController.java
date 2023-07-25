package com.hamgame.hamgame.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hamgame.hamgame.domain.GameNotice;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class GameNoticeController {

	@GetMapping("")
	public List<GameNotice> getMyGameNoticeList() {
		return null;
	}
}
