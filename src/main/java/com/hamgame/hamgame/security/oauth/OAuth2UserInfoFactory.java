package com.hamgame.hamgame.security.oauth;

import java.util.Map;

import com.hamgame.hamgame.security.oauth.provider.Google;
import com.hamgame.hamgame.security.oauth.provider.Kakao;
import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.security.oauth.provider.Naver;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
		if (provider == Provider.KAKAO) {
			return new Kakao(attributes);
		} else if (provider == Provider.NAVER){
			return new Naver(attributes);
		} else if (provider == Provider.GOOGLE){
			return new Google(attributes);
		}
		throw new IllegalArgumentException("Invalid Provider");
	}
}
