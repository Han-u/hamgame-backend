package com.hamgame.hamgame.security.oauth.provider;

import java.util.Map;

import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.security.oauth.OAuth2UserInfo;

public class Naver extends OAuth2UserInfo {

	public Naver(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public Provider getProvider() {
		return Provider.NAVER;
	}

	@Override
	public String getId() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("response");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("id");
	}

	@Override
	public String getEmail() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("response");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("email");
	}

	@Override
	public String getName() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("response");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("nickname");
	}

	@Override
	public String getImageUrl() {
		Map<String, Object> properties = (Map<String, Object>)attributes.get("response");

		if (properties == null) {
			return null;
		}

		return (String)properties.get("profile_image");
	}
}
