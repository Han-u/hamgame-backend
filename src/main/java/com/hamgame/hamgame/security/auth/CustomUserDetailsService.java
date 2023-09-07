package com.hamgame.hamgame.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.domain.user.entity.User;
import com.hamgame.hamgame.domain.user.entity.repository.UserRepository;
import com.hamgame.hamgame.exception.CustomException;
import com.hamgame.hamgame.exception.payload.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return UserPrincipal.create(user);
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserByIdAndEmail(Long id, String email) {
		User user = userRepository.findByIdAndEmail(id, email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return UserPrincipal.create(user);
	}

}
