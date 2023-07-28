package com.hamgame.hamgame.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.User;
import com.hamgame.hamgame.domain.type.Provider;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndProvider(String email, Provider provider);

	Optional<User> findByEmail(String email);
}
