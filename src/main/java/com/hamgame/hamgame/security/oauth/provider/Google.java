package com.hamgame.hamgame.security.oauth.provider;

import java.util.Map;

import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.security.oauth.OAuth2UserInfo;

public class Google extends OAuth2UserInfo {

	public Google(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public Provider getProvider() {
		return Provider.GOOGLE;
	}

	@Override
	public String getId() {
		return (String)attributes.get("sub");
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getImageUrl() {
		return (String)attributes.get("picture");
	}
}
