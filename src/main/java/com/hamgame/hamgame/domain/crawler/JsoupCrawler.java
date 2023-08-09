package com.hamgame.hamgame.domain.crawler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;

public abstract class JsoupCrawler extends Crawler<Document, Element, Elements> {
	public JsoupCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Document process() throws IOException {
		Connection conn = Jsoup.connect(this.url);
		return conn.get();
	}

	@Override
	public List<GameNoticeDto> run(LocalDateTime start, LocalDateTime end) {
		try {
			Document document = process();
			Elements elements = getNoticeList(document);
			List<GameNoticeDto> list = new ArrayList<>();

			for (Element element : elements) {
				if (isNotBetweenDay(element, start, end)) {
					break;
				}
				GameNoticeDto dto = toDto(element);
				list.add(dto);
			}

			Collections.reverse(list);

			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
