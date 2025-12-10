package com.restaurant.dto.response;

import java.util.Set;

/**
 * Authentication response DTO containing JWT token.
 */
public class AuthResponse {

    private String token;
    private String username;
    private Set<String> roles;
    private Long restaurantId;

    public AuthResponse(String token, String username, Set<String> roles, Long restaurantId) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.restaurantId = restaurantId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
