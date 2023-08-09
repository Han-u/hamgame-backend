package com.hamgame.hamgame.domain.crawler.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class OverwatchCrawler extends JsoupCrawler {

	public OverwatchCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.getElementsByTag("blz-card");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.attr("href");
	}

	@Override
	public String getNoticeType(Element element) {
		// return element.attr("analytics-placement");
		return "공지";
	}

	@Override
	public String getTitle(Element element) {
		return element.select("h4").text();
	}

	@Override
	public String getTime(Element element) {
		return element.attr("date");
	}

	@Override
	public String getImageUrl(Element element) {
		return element.select("blz-image").attr("src");
	}

}
