package com.elevideo.backend.user.internal;

import com.elevideo.backend.shared.security.JwtService.JwtData;
import com.elevideo.backend.user.api.dto.AuthenticatedUserResponse;
import com.elevideo.backend.user.api.dto.UserRes;
import com.elevideo.backend.user.internal.model.AccountStatus;
import com.elevideo.backend.user.internal.model.User;
import com.elevideo.backend.auth.api.dto.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = AccountStatus.class)
public interface UserMapper {

    @Mapping(target = "password",       source = "encodedPassword")
    @Mapping(target = "email",          expression = "java(request.email() == null ? null : request.email().toLowerCase())")
    @Mapping(target = "accountStatus",  expression = "java(AccountStatus.ACTIVE)")
    @Mapping(target = "emailVerified",  constant = "false")
    User toUser(RegisterRequest request, String encodedPassword);

    UserRes toUserRes(User user);

    AuthenticatedUserResponse toAuthenticatedUserResponse(User user);

    @Mapping(target = "id", source = "id")
    JwtData toJwtData(User user);
}