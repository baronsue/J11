package com.restaurant.config;

import com.restaurant.model.Customer;
import com.restaurant.model.Restaurant;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.Role;
import com.restaurant.model.RoleName;
import com.restaurant.model.UserAccount;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.RoleRepository;
import com.restaurant.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalTime;

/**
 * Data initializer configuration class, loads sample data on application
 * startup.
 */
@Configuration
@SuppressWarnings("null")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RestaurantRepository restaurantRepository,
            CustomerRepository customerRepository,
            RoleRepository roleRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (restaurantRepository.count() > 0 || customerRepository.count() > 0) {
                return;
            }
            initializeRestaurants(restaurantRepository);
            initializeCustomers(customerRepository);
            initializeSecurity(roleRepository, userAccountRepository, passwordEncoder, restaurantRepository);
        };
    }

    private void initializeRestaurants(RestaurantRepository restaurantRepository) {
        Restaurant restaurant1 = createRestaurant(
                "TanHuo BBQ Central",
                "No.88 Charcoal Rd, Pudong, Shanghai",
                "+86 21 6666 8801",
                "Signature wagyu and Texas-style smokehouse with open grill",
                LocalTime.of(10, 30),
                LocalTime.of(23, 0));
        addTablesToRestaurant(restaurant1, 2, 2, 4, 4, 6, 8);
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = createRestaurant(
                "TanHuo BBQ Riverside",
                "168 Riverside Ave, Huangpu, Shanghai",
                "+86 21 6666 8802",
                "River-view seats, Korean marinades and charcoal seafood",
                LocalTime.of(11, 0),
                LocalTime.of(22, 30));
        addTablesToRestaurant(restaurant2, 2, 4, 4, 6, 6, 10);
        restaurantRepository.save(restaurant2);

        Restaurant restaurant3 = createRestaurant(
                "TanHuo BBQ Prep Lab",
                "Building D, Old Factory Lane, Jing'an, Shanghai",
                "+86 21 6666 8803",
                "Delivery and catering lab with large platters and corporate sets",
                LocalTime.of(9, 0),
                LocalTime.of(21, 0));
        addTablesToRestaurant(restaurant3, 4, 4, 8, 8, 12);
        restaurantRepository.save(restaurant3);
    }

    private Restaurant createRestaurant(String name, String address, String phone,
            String description, LocalTime openingTime, LocalTime closingTime) {
        Restaurant restaurant = new Restaurant(name, address, phone, openingTime, closingTime);
        restaurant.setDescription(description);
        return restaurant;
    }

    private void addTablesToRestaurant(Restaurant restaurant, int... capacities) {
        for (int i = 0; i < capacities.length; i++) {
            String tableNumber = String.format("T%02d", i + 1);
            RestaurantTable table = new RestaurantTable(tableNumber, capacities[i]);
            restaurant.addTable(table);
        }
    }

    private void initializeCustomers(CustomerRepository customerRepository) {
        customerRepository.save(new Customer("Siyu Chen", "siyu.chen@email.com", "+86 138 0000 8801"));
        customerRepository.save(new Customer("Liam Walker", "liam.walker@email.com", "+1 415 555 2011"));
        customerRepository.save(new Customer("Sakura Tanaka", "sakura.tanaka@email.com", "+81 90 1111 2233"));
    }

    private void initializeSecurity(RoleRepository roleRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            RestaurantRepository restaurantRepository) {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(new Role(roleName));
            }
        }

        if (userAccountRepository.count() == 0) {
            UserAccount admin = new UserAccount();
            admin.setUsername("admin");
            admin.setEmail("admin@tanhobbq.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setEnabled(true);
            admin.getRoles().add(roleRepository.findByName(RoleName.SUPER_ADMIN).orElseThrow());
            userAccountRepository.save(admin);

            restaurantRepository.findById(1L).ifPresent(restaurant -> {
                UserAccount manager = new UserAccount();
                manager.setUsername("manager");
                manager.setEmail("manager@tanhobbq.com");
                manager.setPassword(passwordEncoder.encode("Manager@123"));
                manager.setEnabled(true);
                manager.setRestaurant(restaurant);
                manager.getRoles().add(roleRepository.findByName(RoleName.MANAGER).orElseThrow());
                userAccountRepository.save(manager);
            });
        }
    }
}
