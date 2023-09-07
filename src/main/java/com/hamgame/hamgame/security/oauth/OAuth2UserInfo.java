package com.hamgame.hamgame.security.oauth;

import java.util.Map;

import com.hamgame.hamgame.domain.user.entity.Provider;

public abstract class OAuth2UserInfo {
	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public abstract Provider getProvider();

	public abstract String getId();

	public abstract String getEmail();

	public abstract String getName();

	public abstract String getImageUrl();
}
