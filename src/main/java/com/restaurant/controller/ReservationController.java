package com.restaurant.controller;

import com.restaurant.dto.request.CreateReservationRequest;
import com.restaurant.dto.response.ReservationResponse;
import com.restaurant.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reservation controller, handles reservation-related HTTP requests.
 */
@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Reservation management endpoints")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Create new reservation.
     * POST /api/reservations
     */
    @PostMapping
    @Operation(summary = "Create reservation", description = "Create a new reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Table already booked")
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get reservation details.
     * GET /api/reservations/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation", description = "Get reservation details by ID")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long id) {
        ReservationResponse response = reservationService.getReservationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancel reservation.
     * PUT /api/reservations/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel reservation", description = "Cancel a pending or confirmed reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation cancelled"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "409", description = "Cannot cancel reservation")
    })
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
        ReservationResponse response = reservationService.cancelReservation(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Confirm reservation.
     * PUT /api/reservations/{id}/confirm
     */
    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirm reservation", description = "Confirm a pending reservation")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable Long id) {
        ReservationResponse response = reservationService.confirmReservation(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Complete reservation.
     * PUT /api/reservations/{id}/complete
     */
    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete reservation", description = "Mark a confirmed reservation as completed")
    public ResponseEntity<ReservationResponse> completeReservation(@PathVariable Long id) {
        ReservationResponse response = reservationService.completeReservation(id);
        return ResponseEntity.ok(response);
    }
}

