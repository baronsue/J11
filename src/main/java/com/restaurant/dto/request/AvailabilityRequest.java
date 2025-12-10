package com.restaurant.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Availability query request DTO.
 */
public class AvailabilityRequest {

    private static final int MINIMUM_GUESTS = 1;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or in the future")
    private LocalDate date;

    @NotNull(message = "Number of guests is required")
    @Min(value = MINIMUM_GUESTS, message = "Number of guests must be at least 1")
    private Integer numberOfGuests;

    public AvailabilityRequest() {
    }

    public AvailabilityRequest(LocalDate date, Integer numberOfGuests) {
        this.date = date;
        this.numberOfGuests = numberOfGuests;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
