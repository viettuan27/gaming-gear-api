package com.tuanviet.gaminggear.repository;

import com.tuanviet.gaminggear.entity.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByJtiAndRevokedFalseAndExpiresAtAfter(
            UUID jti,
            Instant now
    );
}
