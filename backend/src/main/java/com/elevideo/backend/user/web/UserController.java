package com.elevideo.backend.user.web;

import com.elevideo.backend.shared.web.ApiResult;
import com.elevideo.backend.user.api.UserService;
import com.elevideo.backend.user.api.dto.AuthenticatedUserResponse;
import com.elevideo.backend.user.api.dto.ChangePasswordRequest;
import com.elevideo.backend.user.api.dto.UserUpdateRequest;
import com.elevideo.backend.user.documentation.ChangePasswordEndpointDoc;
import com.elevideo.backend.user.documentation.DeleteUserEndpointDoc;
import com.elevideo.backend.user.documentation.GetMeEndpointDoc;
import com.elevideo.backend.user.documentation.UpdateUserEndpointDoc;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "02 - Usuarios", description = "Gestión de cuentas de usuario")
public class UserController {

    private final UserService userService;

    @GetMeEndpointDoc
    @GetMapping("/me")
    public ResponseEntity<ApiResult<AuthenticatedUserResponse>> getAuthenticatedUser() {
        return ResponseEntity.ok(ApiResult.success(
                userService.getAuthenticatedUser(), "User data retrieved successfully."));
    }

    @UpdateUserEndpointDoc
    @PatchMapping("/me")
    public ResponseEntity<ApiResult<AuthenticatedUserResponse>> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResult.success(
                userService.updateUser(request), "User updated successfully."));
    }

    @ChangePasswordEndpointDoc
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteUserEndpointDoc
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}

