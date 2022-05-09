package com.app.barbershopweb.security;

import com.app.barbershopweb.user.credentials.UserCredentials;
import com.app.barbershopweb.user.crud.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean active;

    public SecurityUser(
            String username,
            String password,
            List<GrantedAuthority> authorities,
            boolean active
    ) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public static UserDetails fromUser(UserCredentials userCredentials, User user) {
        return new SecurityUser(
                userCredentials.getUsername(),
                userCredentials.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())),
                userCredentials.getEnabled()
        );
    }

}