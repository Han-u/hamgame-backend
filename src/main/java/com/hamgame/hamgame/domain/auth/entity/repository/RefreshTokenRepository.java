package com.hamgame.hamgame.domain.auth.entity.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.hamgame.hamgame.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
