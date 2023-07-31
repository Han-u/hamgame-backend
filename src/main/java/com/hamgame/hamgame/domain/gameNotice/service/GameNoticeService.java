package com.hamgame.hamgame.domain.gameNotice.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.entity.repository.GameNoticeRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;
import com.hamgame.hamgame.security.auth.UserPrincipal;

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
