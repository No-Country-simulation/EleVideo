package com.elevideo.backend.user.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRes(
        UUID          id,
        String        firstName,
        String        lastName,
        String        email,
        boolean       emailVerified,
        LocalDateTime createdAt
) {}
