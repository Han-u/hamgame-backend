package com.hamgame.hamgame.config.security.oauth.provider;

import java.util.Map;

import com.hamgame.hamgame.config.security.oauth.OAuth2UserInfo;
import com.hamgame.hamgame.domain.type.Provider;

public class Kakao extends OAuth2UserInfo {

	public Kakao(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getProvider() {
		return Provider.KAKAO.toString();
	}

	@Override
	public String getId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("kakao_account");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("email");
	}

	@Override
	public String getName() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("nickname");
	}

	@Override
	public String getImageUrl() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("thumbnail_image");
	}
}
