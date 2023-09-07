package com.hamgame.hamgame.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.security.auth.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	/*
	 * 리소스 서버에서 사용자 정보를 받아와 최종적으로 서버에서 사용자 정보에 대한 처리를 함
	 * provider별 정보 파싱을 통해 사용자 가입 여부 확인 후 비회원이면 계정 생성
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Provider provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
		OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

		if (userInfo.getEmail().isEmpty()) {
			throw new RuntimeException();
		}

		User user = userRepository.findByEmailAndProvider(userInfo.getEmail(), provider)
			.orElseGet(() -> createUser(userInfo));

		return UserPrincipal.create(user, oAuth2User.getAttributes());
	}

	public User createUser(OAuth2UserInfo userInfo) {
		User user = User.builder()
			.name(userInfo.getName())
			.email(userInfo.getEmail())
			.nickname(userInfo.getName())
			.imageUrl(userInfo.getImageUrl())
			.provider(userInfo.getProvider())
			.build();
		return userRepository.save(user);
	}

}
