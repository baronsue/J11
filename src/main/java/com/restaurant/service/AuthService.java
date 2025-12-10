package com.restaurant.service;

import com.restaurant.dto.request.LoginRequest;
import com.restaurant.dto.request.RegisterRequest;
import com.restaurant.dto.response.AuthResponse;
import com.restaurant.exception.ConflictException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.Role;
import com.restaurant.model.RoleName;
import com.restaurant.model.UserAccount;
import com.restaurant.repository.RoleRepository;
import com.restaurant.repository.UserAccountRepository;
import com.restaurant.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authentication and registration service.
 */
@Service
@Transactional
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final RestaurantService restaurantService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserAccountRepository userAccountRepository,
            RoleRepository roleRepository,
            RestaurantService restaurantService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.restaurantService = restaurantService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        validateUniqueness(request);

        Role role = roleRepository.findByName(request.getRole())
                .orElseGet(() -> roleRepository.save(new Role(request.getRole())));

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        if (request.getRole() == RoleName.MANAGER) {
            if (request.getRestaurantId() == null) {
                throw new ConflictException("Manager must be assigned to a restaurant");
            }
            user.setRestaurant(restaurantService.findRestaurantById(request.getRestaurantId()));
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserAccount savedUser = userAccountRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);
        Set<String> roleNames = savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());
        Long restaurantId = savedUser.getRestaurant() != null ? savedUser.getRestaurant().getId() : null;
        return new AuthResponse(token, savedUser.getUsername(), roleNames, restaurantId);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserAccount user = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.getUsername()));
        String token = jwtTokenProvider.generateToken(user);
        Set<String> roleNames = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());
        Long restaurantId = user.getRestaurant() != null ? user.getRestaurant().getId() : null;
        return new AuthResponse(token, user.getUsername(), roleNames, restaurantId);
    }

    private void validateUniqueness(RegisterRequest request) {
        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }
        if (request.getEmail() != null && userAccountRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
    }
}
