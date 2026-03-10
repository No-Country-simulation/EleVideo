package com.elevideo.backend.auth.internal;

import com.elevideo.backend.auth.api.AuthService;
import com.elevideo.backend.auth.api.dto.*;
import com.elevideo.backend.auth.internal.event.PasswordResetRequestedEvent;
import com.elevideo.backend.auth.internal.event.UserRegisteredEvent;
import com.elevideo.backend.shared.event.DomainEventPublisher;
import com.elevideo.backend.shared.security.CustomUserDetails;
import com.elevideo.backend.shared.security.JwtService;
import com.elevideo.backend.shared.security.JwtService.JwtData;
import com.elevideo.backend.shared.security.TokenPurpose;
import com.elevideo.backend.user.api.UserService;
import com.elevideo.backend.user.internal.UserMapper;
import com.elevideo.backend.user.internal.UserNotFoundException;
import com.elevideo.backend.user.internal.UserRepository;
import com.elevideo.backend.user.internal.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    // --- Config ---
    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${app.frontend.verify-email-path}")
    private String verifyEmailPath;

    @Value("${app.frontend.reset-password-path}")
    private String resetPasswordPath;

    // --- Dependencias ---
    private final UserRepository       userRepository;
    private final UserMapper           userMapper;
    private final UserService          userService;           // interfaz pública del módulo user
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder      passwordEncoder;
    private final JwtService           jwtService;
    private final TokenBlacklistService blacklistService;
    private final DomainEventPublisher  eventPublisher;

    // ----------------------------------------------------------------
    // Casos de uso
    // ----------------------------------------------------------------

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        User user = userMapper.toUser(request, passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String token = generateToken(user, TokenPurpose.EMAIL_VERIFICATION);

        eventPublisher.publish(new UserRegisteredEvent(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                token
        ));

        return new RegisterResponse(user.getEmail());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        return new LoginResponse(
                generateToken(user, TokenPurpose.AUTHENTICATION),
                new LoginResponse.UserSummary(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.isEmailVerified()
                )
        );
    }

    @Override
    @Transactional
    public VerifyEmailResponse verifyEmail(VerifyEmailRequest request) {
        jwtService.validateEmailVerificationToken(request.token());
        UUID userId = jwtService.extractUserId(request.token());

        userService.verifyEmail(userId);
        blacklistToken(request.token());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new VerifyEmailResponse(user.getEmail());
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        // No revela si el email existe — seguridad por diseño
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            String token = generateToken(user, TokenPurpose.PASSWORD_RESET);
            eventPublisher.publish(new PasswordResetRequestedEvent(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    token
            ));
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        jwtService.validateResetPasswordToken(request.token());
        UUID userId = jwtService.extractUserId(request.token());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        blacklistToken(request.token());
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    private String generateToken(User user, TokenPurpose purpose) {
        JwtData jwtData = userMapper.toJwtData(user);
        return jwtService.generateUserToken(jwtData, purpose);
    }

    private void blacklistToken(String token) {
        LocalDateTime expiresAt = jwtService.extractExpiration(token);
        blacklistService.addToBlacklist(token, expiresAt);
    }

    // ----------------------------------------------------------------
    // Excepciones internas del módulo auth
    // ----------------------------------------------------------------

    static final class EmailNotVerifiedException extends com.elevideo.backend.shared.exception.base.DomainException {
        EmailNotVerifiedException() {
            super("Debes verificar tu email antes de iniciar sesión.");
        }
    }
}
