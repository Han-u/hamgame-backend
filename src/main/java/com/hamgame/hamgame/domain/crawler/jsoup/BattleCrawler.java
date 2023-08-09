package com.hamgame.hamgame.domain.crawler.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class BattleCrawler extends JsoupCrawler {
	public BattleCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.select("ul.post-contents__body > li");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.select("a").attr("href"); // no work
	}

	@Override
	public String getNoticeType(Element element) {
		return element.select("dl.post__bottom > dt").text();
	}

	@Override
	public String getTitle(Element element) {
		return element.select("dl.post__description > dt").text();
	}

	@Override
	public String getTime(Element element) {
		return element.select("dl.post__bottom > dd").text();
	}

	@Override
	public String getImageUrl(Element element) {
		return element.select("img").attr("src");
	}

}
