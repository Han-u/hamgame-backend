package com.hamgame.hamgame.domain.crawler.jsoup;

import java.time.LocalDateTime;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.crawler.util.DateFormatter;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class LostArkCrawler extends JsoupCrawler {
	public LostArkCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.select("div.list > ul:nth-child(2) > li");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.select("a").attr("href");
	}

	@Override
	public String getNoticeType(Element element) {
		return element.select("div.list__category").text();
	}

	@Override
	public String getTitle(Element element) {
		return element.select("span.list__title").text();
	}

	@Override
	public String getTime(Element element) {
		return element.select("div.list__date").text();
	}

	@Override
	public String getImageUrl(Element element) {
		return null;
	}

	@Override
	public LocalDateTime getCreateAt(Element element) {
		String time = getTime(element);
		LocalDateTime createAt = LocalDateTime.now();
		if (timeFormat != null && DateFormatter.isValidTimeFormat(time, timeFormat)) {
			createAt = DateFormatter.getAgoTime(time, timeFormat);
		} else if (DateFormatter.isValidDateFormat(time, dateFormat)) {
			createAt = DateFormatter.getDate(time, dateFormat);
		}

		if (isGmt) {
			createAt = DateFormatter.gmtToKst(createAt);
		}
		return createAt;
	}

}
