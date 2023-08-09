package com.hamgame.hamgame.domain.crawler.util;

import java.time.LocalDateTime;
import java.util.List;

import com.hamgame.hamgame.domain.crawler.api.ValorantCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.BattleCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.FifaCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.LolCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.LostArkCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.MapleStoryCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.OverwatchCrawler;
import com.hamgame.hamgame.domain.crawler.jsoup.WowCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;

public class CrawlerFactory {
	public static List<GameNoticeDto> runGameCrawler(GameNoticeConfigDto dto, LocalDateTime start, LocalDateTime end) {
		String game = dto.getCrawlerName();
		switch (game) {
			case "BattleCrawler":
				return new BattleCrawler(dto).run(start, end);
			case "FifaCrawler":
				return new FifaCrawler(dto).run(start, end);
			case "LolCrawler":
				return new LolCrawler(dto).run(start, end);
			case "LostArkCrawler":
				return new LostArkCrawler(dto).run(start, end);
			case "MapleStoryCrawler":
				return new MapleStoryCrawler(dto).run(start, end);
			case "OverwatchCrawler":
				return new OverwatchCrawler(dto).run(start, end);
			case "ValorantCrawler":
				return new ValorantCrawler(dto).run(start, end);
			case "WowCrawler":
				return new WowCrawler(dto).run(start, end);
			default:
				throw new RuntimeException("일치하는 크롤러가 없습니다.");
		}
	}

}
