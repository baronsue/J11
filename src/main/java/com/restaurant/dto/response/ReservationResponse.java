package com.restaurant.dto.response;

import com.restaurant.model.Reservation;
import com.restaurant.model.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Reservation response DTO.
 */
public class ReservationResponse {

    private Long id;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private LocalTime endTime;
    private Integer numberOfGuests;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TableResponse table;
    private String restaurantName;
    private Long restaurantId;
    private CustomerSummary customer;

    public ReservationResponse() {
    }

    public static ReservationResponse fromEntity(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setReservationDate(reservation.getReservationDate());
        response.setReservationTime(reservation.getReservationTime());
        response.setEndTime(reservation.getEndTime());
        response.setNumberOfGuests(reservation.getNumberOfGuests());
        response.setStatus(reservation.getStatus());
        response.setCreatedAt(reservation.getCreatedAt());
        response.setUpdatedAt(reservation.getUpdatedAt());
        response.setTable(TableResponse.fromEntity(reservation.getTable()));
        response.setRestaurantName(reservation.getTable().getRestaurant().getName());
        response.setRestaurantId(reservation.getTable().getRestaurant().getId());
        response.setCustomer(CustomerSummary.fromCustomer(reservation.getCustomer()));
        return response;
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
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
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

    public TableResponse getTable() {
        return table;
    }

    public void setTable(TableResponse table) {
        this.table = table;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public CustomerSummary getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSummary customer) {
        this.customer = customer;
    }

    /**
     * Customer summary inner class.
     */
    public static class CustomerSummary {
        private Long id;
        private String name;
        private String phone;

        public static CustomerSummary fromCustomer(com.restaurant.model.Customer customer) {
            CustomerSummary summary = new CustomerSummary();
            summary.setId(customer.getId());
            summary.setName(customer.getName());
            summary.setPhone(customer.getPhone());
            return summary;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}

