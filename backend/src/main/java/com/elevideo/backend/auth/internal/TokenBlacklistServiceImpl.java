package com.elevideo.backend.auth.internal;

import com.elevideo.backend.auth.internal.model.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final TokenBlacklistRepository repository;

    @Override
    @Transactional
    public void addToBlacklist(String token, LocalDateTime expiresAt) {
        repository.save(new TokenBlacklist(null, token, expiresAt));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        return repository.existsByToken(token);
    }

    /**
     * Limpia tokens expirados cada día a medianoche.
     * Evita que la tabla crezca indefinidamente.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void purgeExpiredTokens() {
        repository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
