package com.restaurant.dto.response;

import com.restaurant.model.Review;

import java.time.LocalDateTime;

/**
 * Review response DTO.
 */
public class ReviewResponse {

    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private String customerName;
    private Long customerId;
    private Long restaurantId;
    private String restaurantName;
    private Long reservationId;

    public ReviewResponse() {
    }

    public static ReviewResponse fromEntity(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setCustomerName(review.getCustomer().getName());
        response.setCustomerId(review.getCustomer().getId());
        response.setRestaurantId(review.getRestaurant().getId());
        response.setRestaurantName(review.getRestaurant().getName());
        response.setReservationId(review.getReservation().getId());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
