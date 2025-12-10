package com.restaurant.service;

import com.restaurant.dto.request.CreateReservationRequest;
import com.restaurant.dto.response.ReservationResponse;
import com.restaurant.exception.ConflictException;
import com.restaurant.exception.InvalidOperationException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.exception.ValidationException;
import com.restaurant.model.Customer;
import com.restaurant.model.Reservation;
import com.restaurant.model.ReservationStatus;
import com.restaurant.model.Restaurant;
import com.restaurant.model.RestaurantTable;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.security.AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Reservation service class, handles reservation-related business logic.
 */
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final AuthorizationService authorizationService;

    public ReservationService(ReservationRepository reservationRepository,
            RestaurantService restaurantService,
            CustomerService customerService,
            AuthorizationService authorizationService) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
        this.customerService = customerService;
        this.authorizationService = authorizationService;
    }

    /**
     * Create new reservation.
     */
    @SuppressWarnings("null")
    public ReservationResponse createReservation(CreateReservationRequest request) {
        Customer customer = customerService.findCustomerById(request.getCustomerId());
        Restaurant restaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        RestaurantTable table = resolveTable(request, restaurant);

        authorizationService.assertRestaurantAccess(restaurant.getId());
        validateReservationRequest(request, restaurant, table);

        Reservation reservation = buildReservation(request, customer, table);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    /**
     * Get reservation details by ID.
     */
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);
        authorizationService.assertRestaurantAccess(reservation.getTable().getRestaurant().getId());
        return ReservationResponse.fromEntity(reservation);
    }

    /**
     * Cancel reservation.
     */
    public ReservationResponse cancelReservation(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);
        authorizationService.assertRestaurantAccess(reservation.getTable().getRestaurant().getId());
        validateCancellable(reservation);
        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    /**
     * Confirm reservation.
     */
    public ReservationResponse confirmReservation(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);
        authorizationService.assertRestaurantAccess(reservation.getTable().getRestaurant().getId());
        validateConfirmable(reservation);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    /**
     * Complete reservation.
     */
    public ReservationResponse completeReservation(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);
        authorizationService.assertRestaurantAccess(reservation.getTable().getRestaurant().getId());
        validateCompletable(reservation);
        reservation.setStatus(ReservationStatus.COMPLETED);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    /**
     * Get customer reservation history.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getCustomerReservations(Long customerId) {
        customerService.findCustomerById(customerId);
        return reservationRepository.findByCustomerId(customerId)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    /**
     * Get all reservations for a restaurant.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getRestaurantReservations(Long restaurantId) {
        restaurantService.findRestaurantById(restaurantId);
        authorizationService.assertRestaurantAccess(restaurantId);
        return reservationRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    @SuppressWarnings("null")
    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));
    }

    private RestaurantTable resolveTable(CreateReservationRequest request, Restaurant restaurant) {
        if (request.getTableId() != null) {
            RestaurantTable table = restaurantService.findTableById(request.getTableId());
            if (!table.getRestaurant().getId().equals(restaurant.getId())) {
                throw new ValidationException("Table does not belong to the specified restaurant");
            }
            return table;
        }
        return findSuitableTable(restaurant, request.getReservationDate(),
                request.getReservationTime(), request.getNumberOfGuests());
    }

    private RestaurantTable findSuitableTable(Restaurant restaurant, LocalDate date,
            LocalTime time, Integer numberOfGuests) {
        return restaurant.getTables().stream()
                .filter(t -> t.getCapacity() >= numberOfGuests)
                .filter(t -> isTableAvailable(t.getId(), date, time))
                .findFirst()
                .orElseThrow(() -> new ConflictException(
                        "No available table for " + numberOfGuests + " guests at the requested time"));
    }

    private boolean isTableAvailable(Long tableId, LocalDate date, LocalTime time) {
        List<Reservation> reservations = reservationRepository.findByTableIdAndReservationDate(tableId, date);
        return reservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .noneMatch(r -> isTimeOverlapping(time, r.getReservationTime()));
    }

    private boolean isTimeOverlapping(LocalTime newTime, LocalTime existingTime) {
        LocalTime newEndTime = newTime.plusHours(Reservation.RESERVATION_DURATION_HOURS);
        LocalTime existingEndTime = existingTime.plusHours(Reservation.RESERVATION_DURATION_HOURS);
        return newTime.isBefore(existingEndTime) && newEndTime.isAfter(existingTime);
    }

    private void validateReservationRequest(CreateReservationRequest request,
            Restaurant restaurant, RestaurantTable table) {
        validateNotInPast(request.getReservationDate(), request.getReservationTime());
        validateWithinOpeningHours(request.getReservationTime(), restaurant);
        validateTableCapacity(request.getNumberOfGuests(), table);
        validateNoDoubleBooking(table.getId(), request.getReservationDate(), request.getReservationTime());
    }

    private void validateNotInPast(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cannot make reservations for past dates or times");
        }
    }

    private void validateWithinOpeningHours(LocalTime time, Restaurant restaurant) {
        LocalTime endTime = time.plusHours(Reservation.RESERVATION_DURATION_HOURS);
        if (time.isBefore(restaurant.getOpeningTime()) || endTime.isAfter(restaurant.getClosingTime())) {
            throw new ValidationException(String.format(
                    "Reservation must be within opening hours (%s - %s)",
                    restaurant.getOpeningTime(), restaurant.getClosingTime()));
        }
    }

    private void validateTableCapacity(Integer numberOfGuests, RestaurantTable table) {
        if (numberOfGuests > table.getCapacity()) {
            throw new ValidationException(String.format(
                    "Number of guests (%d) exceeds table capacity (%d)",
                    numberOfGuests, table.getCapacity()));
        }
    }

    private void validateNoDoubleBooking(Long tableId, LocalDate date, LocalTime time) {
        if (!isTableAvailable(tableId, date, time)) {
            throw new ConflictException("This table is already booked for the selected time slot");
        }
    }

    private void validateCancellable(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.PENDING &&
                reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new InvalidOperationException(String.format(
                    "Cannot cancel reservation with status: %s", reservation.getStatus()));
        }
    }

    private void validateConfirmable(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new InvalidOperationException(String.format(
                    "Cannot confirm reservation with status: %s", reservation.getStatus()));
        }
    }

    private void validateCompletable(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new InvalidOperationException(String.format(
                    "Cannot complete reservation with status: %s", reservation.getStatus()));
        }
    }

    private Reservation buildReservation(CreateReservationRequest request,
            Customer customer, RestaurantTable table) {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setNumberOfGuests(request.getNumberOfGuests());
        reservation.setCustomer(customer);
        reservation.setTable(table);
        reservation.setStatus(ReservationStatus.PENDING);
        return reservation;
    }
}
