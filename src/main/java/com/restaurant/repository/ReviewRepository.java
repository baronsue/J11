package com.restaurant.repository;

import com.restaurant.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Review data access interface.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Get all reviews for a specific restaurant.
     */
    List<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

    /**
     * Get all reviews for a specific customer.
     */
    List<Review> findByCustomerId(Long customerId);

    /**
     * Check if reservation already has a review.
     */
    boolean existsByReservationId(Long reservationId);

    /**
     * Find review by reservation ID.
     */
    Optional<Review> findByReservationId(Long reservationId);

    /**
     * Calculate average rating for restaurant.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double calculateAverageRating(@Param("restaurantId") Long restaurantId);

    /**
     * Count reviews for restaurant.
     */
    long countByRestaurantId(Long restaurantId);
}
