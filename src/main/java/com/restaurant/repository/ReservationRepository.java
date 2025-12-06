package com.restaurant.repository;

import com.restaurant.model.Reservation;
import com.restaurant.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Reservation data access interface.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Get all reservations for a specific customer.
     */
    List<Reservation> findByCustomerId(Long customerId);

    /**
     * Get reservations for a specific customer with specific status.
     */
    List<Reservation> findByCustomerIdAndStatus(Long customerId, ReservationStatus status);

    /**
     * Get all reservations for a specific table on a specific date.
     */
    List<Reservation> findByTableIdAndReservationDate(Long tableId, LocalDate date);

    /**
     * Check if conflicting reservation exists for a table at a specific date and
     * time.
     * Conflict definition: Time slots overlap (assuming 2 hours duration).
     */
    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.table.id = :tableId
            AND r.reservationDate = :date
            AND r.status NOT IN ('CANCELLED')
            AND (
                (:startTime >= r.reservationTime AND :startTime < FUNCTION('DATEADD', 'HOUR', 2, r.reservationTime))
                OR (FUNCTION('DATEADD', 'HOUR', 2, :startTime) > r.reservationTime AND :startTime <= r.reservationTime)
            )
            """)
    boolean existsConflictingReservation(
            @Param("tableId") Long tableId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime);

    /**
     * Get all reservations for a specific restaurant on a specific date.
     */
    @Query("SELECT r FROM Reservation r WHERE r.table.restaurant.id = :restaurantId AND r.reservationDate = :date")
    List<Reservation> findByRestaurantIdAndDate(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date);

    /**
     * Get all reservations for a specific restaurant.
     */
    @Query("SELECT r FROM Reservation r WHERE r.table.restaurant.id = :restaurantId")
    List<Reservation> findByRestaurantId(@Param("restaurantId") Long restaurantId);
}
