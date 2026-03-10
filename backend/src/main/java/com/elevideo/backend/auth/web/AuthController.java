package com.elevideo.backend.auth.web;

import com.elevideo.backend.auth.api.AuthService;
import com.elevideo.backend.auth.api.dto.*;
import com.elevideo.backend.auth.documentation.*;
import com.elevideo.backend.shared.web.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "01 - Autenticación", description = "Endpoints de autenticación y gestión de cuentas")
public class AuthController {

    private final AuthService authService;

    @RegisterEndpointDoc
    @PostMapping("/register")
    public ResponseEntity<ApiResult<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Registration successful. Please check your email to verify your account."));
    }

    @LoginEndpointDoc
    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .ok(ApiResult.success(response, "Login successful."));
    }

    @VerifyEmailEndpointDoc
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResult<VerifyEmailResponse>> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        VerifyEmailResponse response = authService.verifyEmail(request);
        return ResponseEntity
                .ok(ApiResult.success(response, "Email verified successfully."));
    }

    @ForgotPasswordEndpointDoc
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResult<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity
                .ok(ApiResult.success("If that email is registered, you will receive a password reset link shortly."));
    }

    @ResetPasswordEndpointDoc
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResult<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResult.success("Password reset successfully."));
    }
}

