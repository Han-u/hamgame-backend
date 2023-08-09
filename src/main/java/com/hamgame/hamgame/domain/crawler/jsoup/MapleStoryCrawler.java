package com.hamgame.hamgame.domain.crawler.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class MapleStoryCrawler extends JsoupCrawler {
	public MapleStoryCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.select("div.news_board > ul > li");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.select("p > a").attr("href");
	}

	@Override
	public String getNoticeType(Element element) {
		return element.select("p > a > em > img").attr("alt").replaceAll("[\\[\\]]", "");
	}

	@Override
	public String getTitle(Element element) {
		return element.select("span").text();
	}

	@Override
	public String getTime(Element element) {
		return element.select("div.heart_date > dl > dd").text();
	}

	@Override
	public String getImageUrl(Element element) {
		return null;
	}

}
