package com.hamgame.hamgame.domain.crawler.jsoup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.crawler.util.DateFormatter;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;

public class FifaCrawler extends JsoupCrawler {
	public FifaCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.select("div.content > div.list_wrap > div.tbody > div.tr");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.select("a").attr("href");
	}

	@Override
	public String getNoticeType(Element element) {
		return element.select("span.td.sort").text();
	}

	@Override
	public String getTitle(Element element) {
		return element.select("span.td.subject").text();
	}

	@Override
	public String getTime(Element element) {
		return element.select("span.td.date").text();
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
			if (time.contains("전")) {
				createAt = DateFormatter.getAgoDate(time, dateFormat);
			} else {
				createAt = DateFormatter.getDate(time, dateFormat);
			}
		}

		if (isGmt) {
			createAt = DateFormatter.gmtToKst(createAt);
		}
		return createAt;
	}

	@Override
	public List<GameNoticeDto> run(LocalDateTime start, LocalDateTime end) {
		try {
			Document document = process();
			Elements elements = getNoticeList(document);
			List<GameNoticeDto> list = new ArrayList<>();

			for (Element element : elements) {

				if (isNotBetweenDay(element, start, end)) {
					if (isNotice(element)) {
						// 공지일 경우
						continue;
					}
					// 공지 아니면 바로 종료
					break;
				}
				GameNoticeDto dto = toDto(element);
				list.add(dto);
			}

			list.sort(Comparator.comparing(GameNoticeDto::getPostCreatedAt));

			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isNotice(Element element) {
		return element.attr("class").contains("notice");
	}

}
