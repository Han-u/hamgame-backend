package com.hamgame.hamgame.domain.user.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.user.entity.Provider;
import com.hamgame.hamgame.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndProvider(String email, Provider provider);
	
	Optional<User> findByIdAndEmail(Long userId, String email);

	boolean existsByEmailAndProvider(String email, Provider provider);

	boolean existsByNickname(String nickname);
}
