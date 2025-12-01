package com.restaurant.model;

/**
 * Reservation status enum, represents the lifecycle status of a reservation.
 */
public enum ReservationStatus {
    
    /**
     * Pending status - Reservation created but not yet confirmed
     */
    PENDING,
    
    /**
     * Confirmed status - Reservation has been confirmed
     */
    CONFIRMED,
    
    /**
     * Cancelled status - Reservation has been cancelled
     */
    CANCELLED,
    
    /**
     * Completed status - Guest has completed dining
     */
    COMPLETED
}

