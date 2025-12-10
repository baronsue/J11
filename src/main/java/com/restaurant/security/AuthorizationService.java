package com.restaurant.security;

import com.restaurant.model.RoleName;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authorization helper for restaurant scoping.
 */
@Component
public class AuthorizationService {

    public void assertRestaurantAccess(Long restaurantId) {
        if (restaurantId == null) {
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser securityUser)) {
            throw new AccessDeniedException("Unauthorized");
        }
        if (securityUser.hasRole(RoleName.MANAGER)) {
            Long managerRestaurantId = securityUser.getRestaurantId();
            if (managerRestaurantId == null || !managerRestaurantId.equals(restaurantId)) {
                throw new AccessDeniedException("Manager cannot access other restaurants");
            }
            return;
        }
        // Super admin or customer/other roles are allowed to proceed
    }
}
