package com.restaurant.service;

import com.restaurant.dto.request.CreateReviewRequest;
import com.restaurant.dto.response.RestaurantRatingResponse;
import com.restaurant.dto.response.ReviewResponse;
import com.restaurant.exception.ConflictException;
import com.restaurant.exception.InvalidOperationException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.Reservation;
import com.restaurant.model.ReservationStatus;
import com.restaurant.model.Restaurant;
import com.restaurant.model.Review;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Review service class, handles review-related business logic.
 */
@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;

    public ReviewService(ReviewRepository reviewRepository,
                         ReservationRepository reservationRepository,
                         RestaurantService restaurantService) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
    }

    /**
     * Create review. Only completed reservations can be reviewed.
     */
    @SuppressWarnings("null")
    public ReviewResponse createReview(CreateReviewRequest request) {
        Reservation reservation = findReservationById(request.getReservationId());
        
        validateReservationCompleted(reservation);
        validateNoExistingReview(reservation.getId());

        // Ensure associated restaurant information is loaded
        Restaurant restaurant = restaurantService.findRestaurantById(
                reservation.getTable().getRestaurant().getId());

        Review review = buildReview(request, reservation, restaurant);
        Review savedReview = reviewRepository.save(review);
        return ReviewResponse.fromEntity(savedReview);
    }

    /**
     * Get all reviews for a restaurant.
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getRestaurantReviews(Long restaurantId) {
        restaurantService.findRestaurantById(restaurantId);
        return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId)
                .stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    /**
     * Get restaurant rating statistics.
     */
    @Transactional(readOnly = true)
    public RestaurantRatingResponse getRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        Double averageRating = reviewRepository.calculateAverageRating(restaurantId);
        Long totalReviews = reviewRepository.countByRestaurantId(restaurantId);

        return new RestaurantRatingResponse(
                restaurantId,
                restaurant.getName(),
                averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : null,
                totalReviews
        );
    }

    /**
     * Get review by ID.
     */
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        return ReviewResponse.fromEntity(review);
    }

    /**
     * Get all reviews for a customer.
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getCustomerReviews(Long customerId) {
        return reviewRepository.findByCustomerId(customerId)
                .stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    @SuppressWarnings("null")
    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));
    }

    private void validateReservationCompleted(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new InvalidOperationException(
                    "Can only review completed reservations. Current status: " + reservation.getStatus());
        }
    }

    private void validateNoExistingReview(Long reservationId) {
        if (reviewRepository.existsByReservationId(reservationId)) {
            throw new ConflictException("A review already exists for this reservation");
        }
    }

    private Review buildReview(CreateReviewRequest request, Reservation reservation, Restaurant restaurant) {
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setReservation(reservation);
        review.setRestaurant(restaurant);
        review.setCustomer(reservation.getCustomer());
        return review;
    }
}
