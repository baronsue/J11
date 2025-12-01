package com.restaurant.repository;

import com.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Restaurant data access interface.
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Find restaurant by name.
     */
    Optional<Restaurant> findByName(String name);

    /**
     * Check if restaurant with name exists.
     */
    boolean existsByName(String name);
}

