package com.restaurant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Reservation entity, records customer reservation information for tables.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    private static final int DEFAULT_DURATION_HOURS = 2;
    private static final int MINIMUM_GUESTS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Reservation date is required")
    @Column(nullable = false)
    private LocalDate reservationDate;

    @NotNull(message = "Reservation time is required")
    @Column(nullable = false)
    private LocalTime reservationTime;

    @NotNull(message = "Number of guests is required")
    @Min(value = MINIMUM_GUESTS, message = "Number of guests must be at least 1")
    @Column(nullable = false)
    private Integer numberOfGuests;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Reservation() {
        this.status = ReservationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        validateReservationDate(reservationDate);
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        validateReservationTime(reservationTime);
        this.reservationTime = reservationTime;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        validateStatus(status);
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalTime getEndTime() {
        return reservationTime.plusHours(DEFAULT_DURATION_HOURS);
    }

    public LocalDateTime getReservationDateTime() {
        return LocalDateTime.of(reservationDate, reservationTime);
    }

    private void validateReservationDate(LocalDate reservationDate) {
        if (reservationDate == null) {
            throw new IllegalArgumentException("Reservation date cannot be null");
        }
    }

    private void validateReservationTime(LocalTime reservationTime) {
        if (reservationTime == null) {
            throw new IllegalArgumentException("Reservation time cannot be null");
        }
    }

    private void validateNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests == null || numberOfGuests < MINIMUM_GUESTS) {
            throw new IllegalArgumentException(
                    "Number of guests must be at least " + MINIMUM_GUESTS);
        }
    }

    private void validateStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }
}
