package com.restaurant.controller;

import com.restaurant.dto.request.CreateCustomerRequest;
import com.restaurant.dto.response.CustomerResponse;
import com.restaurant.dto.response.ReservationResponse;
import com.restaurant.service.CustomerService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Customer controller, handles customer-related HTTP requests.
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    public CustomerController(CustomerService customerService, ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    /**
     * Register new customer.
     * POST /api/customers
     */
    @PostMapping
    @Operation(summary = "Register customer", description = "Register a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<CustomerResponse> registerCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get customer details.
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get customer", description = "Get customer profile by ID")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all customers list.
     * GET /api/customers
     */
    @GetMapping
    @Operation(summary = "Get all customers", description = "Get a list of all customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Get customer reservation history.
     * GET /api/customers/{id}/reservations
     */
    @GetMapping("/{id}/reservations")
    @Operation(summary = "Get customer reservations", description = "Get all reservations for a customer")
    public ResponseEntity<List<ReservationResponse>> getCustomerReservations(@PathVariable Long id) {
        List<ReservationResponse> reservations = reservationService.getCustomerReservations(id);
        return ResponseEntity.ok(reservations);
    }
}

