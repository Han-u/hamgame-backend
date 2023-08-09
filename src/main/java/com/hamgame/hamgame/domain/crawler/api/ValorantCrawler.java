package com.hamgame.hamgame.domain.crawler.api;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hamgame.hamgame.domain.crawler.ApiCrawler;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;

public class ValorantCrawler extends ApiCrawler {

	public ValorantCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public JSONArray getNoticeList(JSONObject object) {
		return object.getJSONObject("result").getJSONObject("data")
			.getJSONObject("allContentstackArticles").getJSONArray("nodes");
	}

	@Override
	public String getNoticeUrl(JSONObject object) {
		return object.getJSONObject("url").getString("url");
	}

	@Override
	public String getNoticeType(JSONObject object) {
		return object.getJSONArray("category").getJSONObject(0).getString("title");
	}

	@Override
	public String getTitle(JSONObject object) {
		return object.getString("title");
	}

	@Override
	public String getTime(JSONObject object) {
		return object.getString("date");
	}

	@Override
	public String getImageUrl(JSONObject object) {
		return object.getJSONObject("banner").getString("url");
	}
}
