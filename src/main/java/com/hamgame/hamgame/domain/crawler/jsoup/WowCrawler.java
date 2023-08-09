package com.hamgame.hamgame.domain.crawler.jsoup;

import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hamgame.hamgame.domain.crawler.JsoupCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class WowCrawler extends JsoupCrawler {
	public WowCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public Elements getNoticeList(Document document) {
		return document.select("div.List-item");
	}

	@Override
	public String getNoticeUrl(Element element) {
		return element.select("a.NewsBlog-link").attr("href");
	}

	@Override
	public String getNoticeType(Element element) {
		return "공지";
	}

	@Override
	public String getTitle(Element element) {
		return element.select("div.NewsBlog-title").text();
	}

	@Override
	public String getTime(Element element) {
		String arr = element.select("div.NewsBlog-date").attr("data-props");
		JSONObject object = new JSONObject(arr);
		return (String)object.get("iso8601");
	}

	@Override
	public String getImageUrl(Element element) {
		return element.select("img.NewsBlog-image").attr("data-src");
	}

}
