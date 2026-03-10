package com.elevideo.backend.shared.security;

import com.elevideo.backend.user.internal.model.User;
import com.elevideo.backend.user.internal.model.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public UUID getId()   { return user.getId(); }
    public User getUser() { return user; }

    @Override public String getUsername()  { return user.getEmail(); }
    @Override public String getPassword()  { return user.getPassword(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override public boolean isAccountNonExpired()   { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountStatus() != AccountStatus.BLOCKED;
    }

    @Override
    public boolean isEnabled() {
        return user.getAccountStatus() == AccountStatus.ACTIVE;
    }
}