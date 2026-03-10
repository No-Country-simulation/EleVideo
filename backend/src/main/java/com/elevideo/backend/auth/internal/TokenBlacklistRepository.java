package com.elevideo.backend.auth.internal;

import com.elevideo.backend.auth.internal.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    boolean existsByToken(String token);

    @Modifying
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
