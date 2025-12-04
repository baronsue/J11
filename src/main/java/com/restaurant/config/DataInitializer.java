package com.restaurant.config;

import com.restaurant.model.Customer;
import com.restaurant.model.Restaurant;
import com.restaurant.model.RestaurantTable;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

/**
 * Data initializer configuration class, loads sample data on application startup.
 */
@Configuration
@SuppressWarnings("null")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RestaurantRepository restaurantRepository,
                                   CustomerRepository customerRepository) {
        return args -> {
            initializeRestaurants(restaurantRepository);
            initializeCustomers(customerRepository);
        };
    }

    private void initializeRestaurants(RestaurantRepository restaurantRepository) {
        Restaurant restaurant1 = createRestaurant(
                "Le Petit Bistro",
                "123 Main Street, Paris",
                "+33 1 23 45 67 89",
                "A cozy French bistro serving authentic cuisine",
                LocalTime.of(11, 0),
                LocalTime.of(23, 0)
        );
        addTablesToRestaurant(restaurant1, 2, 4, 6);
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = createRestaurant(
                "Dragon Palace",
                "456 Dragon Road, Shanghai",
                "+86 21 8765 4321",
                "Premium Chinese restaurant with traditional flavors",
                LocalTime.of(10, 0),
                LocalTime.of(22, 0)
        );
        addTablesToRestaurant(restaurant2, 2, 4, 8, 10);
        restaurantRepository.save(restaurant2);

        Restaurant restaurant3 = createRestaurant(
                "Bella Italia",
                "789 Roma Avenue, Milan",
                "+39 02 1234 5678",
                "Authentic Italian restaurant with homemade pasta",
                LocalTime.of(12, 0),
                LocalTime.of(23, 0)
        );
        addTablesToRestaurant(restaurant3, 2, 2, 4, 4, 6);
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
        customerRepository.save(new Customer("John Smith", "john.smith@email.com", "+1 555-0101"));
        customerRepository.save(new Customer("Marie Dupont", "marie.dupont@email.com", "+33 6 12 34 56 78"));
        customerRepository.save(new Customer("Zhang Wei", "zhang.wei@email.com", "+86 138 0000 0001"));
    }
}

