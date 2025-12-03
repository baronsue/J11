package com.restaurant.dto.response;

/**
 * Restaurant rating response DTO.
 */
public class RestaurantRatingResponse {

    private Long restaurantId;
    private String restaurantName;
    private Double averageRating;
    private Long totalReviews;

    public RestaurantRatingResponse() {
    }

    public RestaurantRatingResponse(Long restaurantId, String restaurantName, 
                                    Double averageRating, Long totalReviews) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }
}
