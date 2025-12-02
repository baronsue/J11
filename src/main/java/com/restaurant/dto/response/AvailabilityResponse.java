package com.restaurant.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Availability response DTO.
 */
public class AvailabilityResponse {

    private Long restaurantId;
    private String restaurantName;
    private LocalDate date;
    private List<TimeSlot> availableTimeSlots;

    public AvailabilityResponse() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<TimeSlot> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    /**
     * Time slot inner class.
     */
    public static class TimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;
        private List<TableResponse> availableTables;

        public TimeSlot() {
        }

        public TimeSlot(LocalTime startTime, LocalTime endTime, List<TableResponse> availableTables) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.availableTables = availableTables;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        public List<TableResponse> getAvailableTables() {
            return availableTables;
        }

        public void setAvailableTables(List<TableResponse> availableTables) {
            this.availableTables = availableTables;
        }
    }
}

