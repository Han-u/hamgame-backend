package com.hamgame.hamgame.domain.gameNotice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.RestPage;
import com.hamgame.hamgame.domain.crawler.util.CrawlerFactory;
import com.hamgame.hamgame.domain.game.entity.Game;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;
import com.hamgame.hamgame.domain.gameNotice.entity.GameNotice;
import com.hamgame.hamgame.domain.gameNotice.entity.repository.GameNoticeConfigRepository;
import com.hamgame.hamgame.domain.gameNotice.entity.repository.GameNoticeRepository;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameNoticeService {
	private final GameNoticeRepository gameNoticeRepository;
	private final UserRepository userRepository;

	private final GameNoticeConfigRepository gameNoticeConfigRepository;

	@Cacheable(value = "favoriteGameNotice", key = "#userId")
	@Transactional(readOnly = true)
	public Page<GameNoticeDto> getMyGameNoticeList(Pageable pageable, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Set<Game> games = user.getGames();
		return new RestPage<>(gameNoticeRepository.findByGameIn(games, pageable).map(GameNoticeDto::of));
	}

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	@CacheEvict(value = "favoriteGameNotice", allEntries = true)
	public void runCrawler() {
		// 실행할 크롤러 설정 정보
		List<GameNoticeConfigDto> config = gameNoticeConfigRepository.findAll()
			.stream()
			.map(GameNoticeConfigDto::of)
			.collect(Collectors.toList());
		List<GameNotice> newNotices = new ArrayList<>();

		// 매일 12시에 실행하여 어제의 게임 공지 목록 긁어옴
		LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
		LocalDateTime end = LocalDate.now().atStartOfDay();

		for (GameNoticeConfigDto c : config) {
			List<GameNoticeDto> result = CrawlerFactory.runGameCrawler(c, start, end);
			newNotices.addAll(result.stream().map(GameNoticeDto::toEntity).collect(Collectors.toList()));
		}
		gameNoticeRepository.saveAll(newNotices);
	}
}
