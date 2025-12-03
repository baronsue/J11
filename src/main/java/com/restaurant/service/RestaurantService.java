package com.restaurant.service;

import com.restaurant.dto.response.AvailabilityResponse;
import com.restaurant.dto.response.RestaurantResponse;
import com.restaurant.dto.response.TableResponse;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.exception.ValidationException;
import com.restaurant.model.Restaurant;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.Reservation;
import com.restaurant.model.ReservationStatus;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Restaurant service class, handles restaurant and table related business
 * logic.
 */
@Service
@Transactional
public class RestaurantService {

    private static final int TIME_SLOT_DURATION_HOURS = 2;

    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
            RestaurantTableRepository tableRepository,
            ReservationRepository reservationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Get all restaurants list (summary).
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(RestaurantResponse::fromEntitySummary)
                .toList();
    }

    /**
     * Get restaurant details by ID (including tables).
     */
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        return RestaurantResponse.fromEntity(restaurant);
    }

    /**
     * Get all tables for a restaurant.
     */
    @Transactional(readOnly = true)
    public List<TableResponse> getTablesByRestaurantId(Long restaurantId) {
        validateRestaurantExists(restaurantId);
        return tableRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(TableResponse::fromEntity)
                .toList();
    }

    /**
     * Check restaurant availability on a specific date.
     */
    @Transactional(readOnly = true)
    public AvailabilityResponse checkAvailability(Long restaurantId, LocalDate date, Integer numberOfGuests) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        validateFutureDate(date);

        Integer guestCount = (numberOfGuests != null && numberOfGuests > 0) ? numberOfGuests : 1;
        List<AvailabilityResponse.TimeSlot> availableSlots = generateAvailableTimeSlots(restaurant, date, guestCount);

        AvailabilityResponse response = new AvailabilityResponse();
        response.setRestaurantId(restaurantId);
        response.setRestaurantName(restaurant.getName());
        response.setDate(date);
        response.setAvailableTimeSlots(availableSlots);
        return response;
    }

    /**
     * Internal method: Find restaurant entity by ID.
     */
    @SuppressWarnings("null")
    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", restaurantId));
    }

    /**
     * Internal method: Find restaurant table entity by ID.
     */
    @SuppressWarnings("null")
    public RestaurantTable findTableById(Long tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", tableId));
    }

    private List<AvailabilityResponse.TimeSlot> generateAvailableTimeSlots(
            Restaurant restaurant, LocalDate date, Integer numberOfGuests) {
        List<AvailabilityResponse.TimeSlot> timeSlots = new ArrayList<>();
        LocalTime currentSlot = restaurant.getOpeningTime();
        LocalTime closingTime = restaurant.getClosingTime();

        List<Reservation> existingReservations = reservationRepository
                .findByRestaurantIdAndDate(restaurant.getId(), date);

        while (currentSlot.plusHours(TIME_SLOT_DURATION_HOURS).compareTo(closingTime) <= 0) {
            List<TableResponse> availableTables = findAvailableTablesForSlot(
                    restaurant, date, currentSlot, numberOfGuests, existingReservations);

            if (!availableTables.isEmpty()) {
                AvailabilityResponse.TimeSlot slot = new AvailabilityResponse.TimeSlot(
                        currentSlot, currentSlot.plusHours(TIME_SLOT_DURATION_HOURS), availableTables);
                timeSlots.add(slot);
            }
            currentSlot = currentSlot.plusHours(TIME_SLOT_DURATION_HOURS);
        }
        return timeSlots;
    }

    private List<TableResponse> findAvailableTablesForSlot(
            Restaurant restaurant, LocalDate date, LocalTime slotTime,
            Integer numberOfGuests, List<Reservation> existingReservations) {
        return restaurant.getTables().stream()
                .filter(table -> table.getCapacity() >= numberOfGuests)
                .filter(table -> isTableAvailableForSlot(table, date, slotTime, existingReservations))
                .map(TableResponse::fromEntity)
                .toList();
    }

    private boolean isTableAvailableForSlot(RestaurantTable table, LocalDate date,
            LocalTime slotTime, List<Reservation> reservations) {
        return reservations.stream()
                .filter(r -> r.getTable().getId().equals(table.getId()))
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .noneMatch(r -> isTimeOverlapping(slotTime, r.getReservationTime()));
    }

    private boolean isTimeOverlapping(LocalTime newStartTime, LocalTime existingStartTime) {
        LocalTime newEndTime = newStartTime.plusHours(TIME_SLOT_DURATION_HOURS);
        LocalTime existingEndTime = existingStartTime.plusHours(TIME_SLOT_DURATION_HOURS);
        return newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime);
    }

    @SuppressWarnings("null")
    private void validateRestaurantExists(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant", "id", restaurantId);
        }
    }

    private void validateFutureDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException("Date must be today or in the future");
        }
    }
}
