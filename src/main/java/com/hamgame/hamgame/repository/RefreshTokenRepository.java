package com.hamgame.hamgame.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamgame.hamgame.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
