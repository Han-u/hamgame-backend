package com.hamgame.hamgame.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.Game;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.dto.GameNoticeDto;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.ErrorCode;
import com.hamgame.hamgame.repository.GameNoticeRepository;
import com.hamgame.hamgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameNoticeService {
	private final GameNoticeRepository gameNoticeRepository;
	private final UserRepository userRepository;

	public Page<GameNoticeDto> getMyGameNoticeList(Pageable pageable, UserPrincipal userPrincipal) {
		User user = userRepository.findById(userPrincipal.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Set<Game> games = user.getGames();
		return gameNoticeRepository.findByGameIn(games, pageable).map(GameNoticeDto::of);

	}
}
