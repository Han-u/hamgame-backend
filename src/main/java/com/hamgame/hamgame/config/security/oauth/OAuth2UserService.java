package com.hamgame.hamgame.config.security.oauth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hamgame.hamgame.config.security.auth.UserPrincipal;
import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.domain.type.Provider;
import com.hamgame.hamgame.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Provider provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
		OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

		if (userInfo.getEmail().isEmpty()) {
			throw new RuntimeException();
		}

		Optional<User> optionalUser = userRepository.findByEmailAndProvider(userInfo.getEmail(), provider);
		User user;

		if (optionalUser.isPresent()) {
			user = updateUser(optionalUser.get(), userInfo);
		} else {
			user = createUser(userInfo, provider);
		}

		return UserPrincipal.create(user, oAuth2User.getAttributes());
	}

	public OAuth2User loadUserById(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() ->
				new UsernameNotFoundException("유저 정보를 찾을 수 없습니다.")
			);

		return UserPrincipal.create(user);
	}

	public User createUser(OAuth2UserInfo userInfo, Provider provider) {
		User user = User.builder()
			.name(userInfo.getName())
			.email(userInfo.getEmail())
			.nickname(userInfo.getName())
			.imageUrl(userInfo.getImageUrl())
			.provider(provider)
			.build();
		return userRepository.save(user);
	}

	public User updateUser(User user, OAuth2UserInfo userInfo) {
		user.updateName(userInfo.getName());
		user.updateImageUrl(user.getImageUrl());
		return userRepository.save(user);
	}
}
