package com.restaurant.controller;

import com.restaurant.dto.request.CreateReviewRequest;
import com.restaurant.dto.response.RestaurantRatingResponse;
import com.restaurant.dto.response.ReviewResponse;
import com.restaurant.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Review controller, handles review-related HTTP requests.
 */
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Review management APIs")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Create review.
     * POST /api/reviews
     */
    @PostMapping
    @Operation(summary = "Create a review", description = "Create a review for a completed reservation")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get review details.
     * GET /api/reviews/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Get review details by its ID")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all reviews for a restaurant.
     * GET /api/reviews/restaurant/{restaurantId}
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get restaurant reviews", description = "Get all reviews for a specific restaurant")
    public ResponseEntity<List<ReviewResponse>> getRestaurantReviews(@PathVariable Long restaurantId) {
        List<ReviewResponse> reviews = reviewService.getRestaurantReviews(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get restaurant rating statistics.
     * GET /api/reviews/restaurant/{restaurantId}/rating
     */
    @GetMapping("/restaurant/{restaurantId}/rating")
    @Operation(summary = "Get restaurant rating", description = "Get average rating and total reviews for a restaurant")
    public ResponseEntity<RestaurantRatingResponse> getRestaurantRating(@PathVariable Long restaurantId) {
        RestaurantRatingResponse response = reviewService.getRestaurantRating(restaurantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all reviews for a customer.
     * GET /api/reviews/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer reviews", description = "Get all reviews written by a specific customer")
    public ResponseEntity<List<ReviewResponse>> getCustomerReviews(@PathVariable Long customerId) {
        List<ReviewResponse> reviews = reviewService.getCustomerReviews(customerId);
        return ResponseEntity.ok(reviews);
    }
}
