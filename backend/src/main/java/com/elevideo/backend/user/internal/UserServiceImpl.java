package com.elevideo.backend.user.internal;

import com.elevideo.backend.shared.event.DomainEventPublisher;
import com.elevideo.backend.shared.security.CurrentUserProvider;
import com.elevideo.backend.user.api.UserService;
import com.elevideo.backend.user.api.dto.AuthenticatedUserResponse;
import com.elevideo.backend.user.api.dto.ChangePasswordRequest;

import com.elevideo.backend.user.api.dto.UserUpdateRequest;
import com.elevideo.backend.user.internal.event.UserEmailVerifiedEvent;
import com.elevideo.backend.user.internal.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository       userRepository;
    private final UserMapper           userMapper;
    private final CurrentUserProvider  currentUserProvider;
    private final PasswordEncoder      passwordEncoder;
    private final DomainEventPublisher eventPublisher;

    // ----------------------------------------------------------------
    // Casos de uso
    // ----------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUserResponse getAuthenticatedUser() {
        return userMapper.toAuthenticatedUserResponse(findCurrentUser());
    }



    @Override
    @Transactional
    public AuthenticatedUserResponse updateUser(UserUpdateRequest request) {
        User user = findCurrentUser();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        userRepository.save(user);
        return userMapper.toAuthenticatedUserResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = findCurrentUser();

        // Verifica que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        // Evita cambiar a la misma contraseña
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new SamePasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser() {
        UUID userId = currentUserProvider.getCurrentUserId();
        userRepository.deleteById(userId);
    }

    // ----------------------------------------------------------------
    // Métodos llamados desde otros módulos (auth)
    // ----------------------------------------------------------------

    /**
     * Marca el email del usuario como verificado y publica el evento correspondiente.
     * Llamado desde AuthServiceImpl tras validar el token de verificación.
     */
    @Override
    @Transactional
    public void verifyEmail(UUID userId) {
        User user = findUserById(userId);

        if (user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        eventPublisher.publish(new UserEmailVerifiedEvent(user.getId(), user.getEmail()));
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    private User findCurrentUser() {
        return findUserById(currentUserProvider.getCurrentUserId());
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

}

