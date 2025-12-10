package com.restaurant.security;

import com.restaurant.model.Role;
import com.restaurant.model.RoleName;
import com.restaurant.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security user details wrapper.
 */
public class SecurityUser implements UserDetails {

    private final UserAccount user;
    private final Set<GrantedAuthority> authorities;

    public SecurityUser(UserAccount user) {
        this.user = user;
        this.authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    public Long getUserId() {
        return user.getId();
    }

    public Long getRestaurantId() {
        return user.getRestaurant() != null ? user.getRestaurant().getId() : null;
    }

    public boolean hasRole(RoleName roleName) {
        return user.hasRole(roleName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
