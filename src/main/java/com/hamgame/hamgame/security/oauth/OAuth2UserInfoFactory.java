package com.hamgame.hamgame.security.oauth;

import java.util.Map;

import com.hamgame.hamgame.security.oauth.provider.Kakao;
import com.hamgame.hamgame.domain.user.entity.Provider;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
		if (provider == Provider.KAKAO) {
			return new Kakao(attributes);
		}

		// 나머지 나중에
		throw new IllegalArgumentException("Invalid Provider");
	}
}
