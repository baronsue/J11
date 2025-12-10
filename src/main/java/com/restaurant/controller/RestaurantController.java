package com.restaurant.controller;

import com.restaurant.dto.response.AvailabilityResponse;
import com.restaurant.dto.response.ReservationResponse;
import com.restaurant.dto.response.RestaurantResponse;
import com.restaurant.dto.response.TableResponse;
import com.restaurant.service.ReservationService;
import com.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Restaurant controller, handles restaurant-related HTTP requests.
 */
@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "Restaurant management endpoints")
@Validated
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReservationService reservationService;

    public RestaurantController(RestaurantService restaurantService, ReservationService reservationService) {
        this.restaurantService = restaurantService;
        this.reservationService = reservationService;
    }

    /**
     * Get all restaurants list.
     * GET /api/restaurants
     */
    @GetMapping
    @Operation(summary = "Get all restaurants", description = "Retrieve a list of all restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        List<RestaurantResponse> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    /**
     * Get restaurant details (including tables).
     * GET /api/restaurants/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID", description = "Retrieve restaurant details including tables")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<RestaurantResponse> getRestaurant(
            @Parameter(description = "Restaurant ID") @PathVariable Long id) {
        RestaurantResponse response = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all tables for a restaurant.
     * GET /api/restaurants/{id}/tables
     */
    @GetMapping("/{id}/tables")
    public ResponseEntity<List<TableResponse>> getRestaurantTables(@PathVariable Long id) {
        List<TableResponse> tables = restaurantService.getTablesByRestaurantId(id);
        return ResponseEntity.ok(tables);
    }

    /**
     * Check restaurant availability on a specific date.
     * GET /api/restaurants/{id}/availability?date=2025-12-25&guests=4
     */
    @GetMapping("/{id}/availability")
    @Operation(summary = "Check availability", description = "Check restaurant availability for a specific date")
    public ResponseEntity<AvailabilityResponse> checkAvailability(
            @Parameter(description = "Restaurant ID") @PathVariable Long id,
            @Parameter(description = "Date to check (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull @FutureOrPresent LocalDate date,
            @Parameter(description = "Number of guests") @RequestParam(required = false, defaultValue = "1") @Min(1) Integer guests) {
        AvailabilityResponse response = restaurantService.checkAvailability(id, date, guests);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all reservations for a restaurant.
     * GET /api/restaurants/{id}/reservations
     */
    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationResponse>> getRestaurantReservations(@PathVariable Long id) {
        List<ReservationResponse> reservations = reservationService.getRestaurantReservations(id);
        return ResponseEntity.ok(reservations);
    }
}
