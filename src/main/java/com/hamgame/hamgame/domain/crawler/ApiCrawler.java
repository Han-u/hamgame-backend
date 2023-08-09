package com.hamgame.hamgame.domain.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeConfigDto;
import com.hamgame.hamgame.domain.gameNotice.dto.GameNoticeDto;

public abstract class ApiCrawler extends Crawler<JSONObject, JSONObject, JSONArray> {
	public ApiCrawler(GameNoticeConfigDto dto) {
		super(dto);
	}

	@Override
	public JSONObject process() throws IOException {
		URL valUrl = new URL(this.url);
		URLConnection con = valUrl.openConnection();
		StringBuilder response = new StringBuilder();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String line;
			while ((line = in.readLine()) != null) {
				response.append(line);
			}
		}
		return new JSONObject(response.toString());
	}

	@Override
	public List<GameNoticeDto> run(LocalDateTime start, LocalDateTime end) {
		try {
			JSONObject object = process();
			JSONArray elements = getNoticeList(object);
			List<GameNoticeDto> list = new ArrayList<>();

			for (int i = 0; i < elements.length(); i++) {
				JSONObject element = elements.getJSONObject(i);
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
