package com.hamgame.hamgame.domain.crawler.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class LolCrawler extends JsoupCrawler {
	public LolCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.select("ol > li");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.select("a").attr("href");
	}

	@Override
	public String getNoticeType(Element element) {
		return element.select("div[class^=style__Category]").text();
	}

	@Override
	public String getTitle(Element element) {
		return element.select("h2").text();
	}

	@Override
	public String getTime(Element element) {
		return element.select("time").attr("datetime");
	}

	@Override
	public String getImageUrl(Element element) {
		return element.select("img").attr("src");
	}

}
