package com.restaurant.repository;

import com.restaurant.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
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

    /**
     * Find available tables for specific date and time (excluding booked tables).
     */
    @Query("""
            SELECT t FROM RestaurantTable t
            WHERE t.restaurant.id = :restaurantId
            AND t.capacity >= :numberOfGuests
            AND t.id NOT IN (
                SELECT r.table.id FROM Reservation r
                WHERE r.table.restaurant.id = :restaurantId
                AND r.reservationDate = :date
                AND r.status NOT IN ('CANCELLED')
                AND (
                    (r.reservationTime <= :time AND :time < FUNCTION('DATEADD', 'HOUR', 2, r.reservationTime))
                    OR (r.reservationTime < FUNCTION('DATEADD', 'HOUR', 2, :time) AND :time <= r.reservationTime)
                )
            )
            """)
    List<RestaurantTable> findAvailableTables(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("numberOfGuests") Integer numberOfGuests);
}
