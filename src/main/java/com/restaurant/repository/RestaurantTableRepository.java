package com.restaurant.repository;

import com.restaurant.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Restaurant table data access interface.
 */
@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    /**
     * Get all tables for a specific restaurant.
     */
    List<RestaurantTable> findByRestaurantId(Long restaurantId);

    /**
     * Find table by restaurant ID and table number.
     */
    Optional<RestaurantTable> findByRestaurantIdAndTableNumber(Long restaurantId, String tableNumber);

    /**
     * Find tables in restaurant with capacity greater than or equal to required
     * guests.
     */
    List<RestaurantTable> findByRestaurantIdAndCapacityGreaterThanEqual(Long restaurantId, Integer capacity);

}
