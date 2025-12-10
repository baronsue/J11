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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ReservationService.
 */
@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private CustomerService customerService;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private ReservationService reservationService;

    private Customer testCustomer;
    private Restaurant testRestaurant;
    private RestaurantTable testTable;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testCustomer = createTestCustomer();
        testRestaurant = createTestRestaurant();
        testTable = createTestTable(testRestaurant);
        testReservation = createTestReservation(testCustomer, testTable);
    }

    @Test
    @DisplayName("Should create reservation successfully when all validations pass")
    void shouldCreateReservationSuccessfully() {
        // Given
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRestaurantId(1L);
        request.setCustomerId(1L);
        request.setReservationDate(LocalDate.now().plusDays(7));
        request.setReservationTime(LocalTime.of(19, 0));
        request.setNumberOfGuests(2);

        when(customerService.findCustomerById(1L)).thenReturn(testCustomer);
        when(restaurantService.findRestaurantById(1L)).thenReturn(testRestaurant);
        when(reservationRepository.findByTableIdAndReservationDate(any(), any()))
                .thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // When
        ReservationResponse response = reservationService.createReservation(request);

        // Then
        assertNotNull(response);
        assertEquals(ReservationStatus.PENDING, response.getStatus());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when table is already booked")
    void shouldThrowConflictExceptionWhenTableAlreadyBooked() {
        // Given
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRestaurantId(1L);
        request.setTableId(1L);
        request.setCustomerId(1L);
        request.setReservationDate(LocalDate.now().plusDays(7));
        request.setReservationTime(LocalTime.of(19, 0));
        request.setNumberOfGuests(2);

        Reservation existingReservation = createTestReservation(testCustomer, testTable);
        existingReservation.setReservationTime(LocalTime.of(19, 0));
        existingReservation.setStatus(ReservationStatus.CONFIRMED);

        when(customerService.findCustomerById(1L)).thenReturn(testCustomer);
        when(restaurantService.findRestaurantById(1L)).thenReturn(testRestaurant);
        when(restaurantService.findTableById(1L)).thenReturn(testTable);
        when(reservationRepository.findByTableIdAndReservationDate(any(), any()))
                .thenReturn(List.of(existingReservation));

        // When & Then
        assertThrows(ConflictException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when booking outside opening hours")
    void shouldThrowValidationExceptionWhenOutsideOpeningHours() {
        // Given
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRestaurantId(1L);
        request.setCustomerId(1L);
        request.setReservationDate(LocalDate.now().plusDays(7));
        request.setReservationTime(LocalTime.of(8, 0)); // Before opening time
        request.setNumberOfGuests(2);

        when(customerService.findCustomerById(1L)).thenReturn(testCustomer);
        when(restaurantService.findRestaurantById(1L)).thenReturn(testRestaurant);
        when(reservationRepository.findByTableIdAndReservationDate(any(), any()))
                .thenReturn(new ArrayList<>());

        // When & Then
        assertThrows(ValidationException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when guests exceed table capacity")
    void shouldThrowValidationExceptionWhenExceedingCapacity() {
        // Given
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRestaurantId(1L);
        request.setTableId(1L);
        request.setCustomerId(1L);
        request.setReservationDate(LocalDate.now().plusDays(7));
        request.setReservationTime(LocalTime.of(19, 0));
        request.setNumberOfGuests(10); // Exceeds table capacity of 4

        when(customerService.findCustomerById(1L)).thenReturn(testCustomer);
        when(restaurantService.findRestaurantById(1L)).thenReturn(testRestaurant);
        when(restaurantService.findTableById(1L)).thenReturn(testTable);

        // When & Then
        assertThrows(ValidationException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should cancel reservation successfully when status is PENDING")
    void shouldCancelReservationSuccessfully() {
        // Given
        testReservation.setStatus(ReservationStatus.PENDING);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // When
        ReservationResponse response = reservationService.cancelReservation(1L);

        // Then
        assertEquals(ReservationStatus.CANCELLED, response.getStatus());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    @DisplayName("Should throw InvalidOperationException when cancelling completed reservation")
    void shouldThrowInvalidOperationExceptionWhenCancellingCompletedReservation() {
        // Given
        testReservation.setStatus(ReservationStatus.COMPLETED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        // When & Then
        assertThrows(InvalidOperationException.class, () -> reservationService.cancelReservation(1L));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should confirm reservation successfully when status is PENDING")
    void shouldConfirmReservationSuccessfully() {
        // Given
        testReservation.setStatus(ReservationStatus.PENDING);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // When
        ReservationResponse response = reservationService.confirmReservation(1L);

        // Then
        assertEquals(ReservationStatus.CONFIRMED, response.getStatus());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    @DisplayName("Should reject confirmation when status is not PENDING")
    void shouldRejectConfirmationWhenNotPending() {
        // Given
        testReservation.setStatus(ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        // When & Then
        assertThrows(InvalidOperationException.class, () -> reservationService.confirmReservation(1L));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when reservation not found")
    void shouldThrowResourceNotFoundExceptionWhenReservationNotFound() {
        // Given
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(999L));
    }

    @Test
    @DisplayName("Should get customer reservations successfully")
    void shouldGetCustomerReservationsSuccessfully() {
        // Given
        List<Reservation> reservations = List.of(testReservation);
        when(customerService.findCustomerById(1L)).thenReturn(testCustomer);
        when(reservationRepository.findByCustomerId(1L)).thenReturn(reservations);

        // When
        List<ReservationResponse> responses = reservationService.getCustomerReservations(1L);

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(reservationRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    @DisplayName("Should complete reservation successfully when status is CONFIRMED")
    void shouldCompleteReservationSuccessfully() {
        // Given
        testReservation.setStatus(ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // When
        ReservationResponse response = reservationService.completeReservation(1L);

        // Then
        assertEquals(ReservationStatus.COMPLETED, response.getStatus());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    // Helper methods to create test data
    private Customer createTestCustomer() {
        Customer customer = new Customer("John Smith", "john@email.com", "+1 555-0101");
        customer.setId(1L);
        return customer;
    }

    private Restaurant createTestRestaurant() {
        Restaurant restaurant = new Restaurant(
                "Test Restaurant",
                "123 Test Street",
                "+1 555-0000",
                LocalTime.of(10, 0),
                LocalTime.of(22, 0));
        restaurant.setId(1L);
        return restaurant;
    }

    private RestaurantTable createTestTable(Restaurant restaurant) {
        RestaurantTable table = new RestaurantTable("T01", 4);
        table.setId(1L);
        restaurant.addTable(table);
        return table;
    }

    private Reservation createTestReservation(Customer customer, RestaurantTable table) {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationDate(LocalDate.now().plusDays(7));
        reservation.setReservationTime(LocalTime.of(19, 0));
        reservation.setNumberOfGuests(2);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCustomer(customer);
        reservation.setTable(table);
        return reservation;
    }

    @Test
    @DisplayName("Should fail when table does not belong to restaurant")
    void shouldFailWhenTableNotInRestaurant() {
        // Given
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRestaurantId(99L);
        request.setTableId(1L);
        request.setCustomerId(1L);
        request.setReservationDate(LocalDate.now().plusDays(2));
        request.setReservationTime(LocalTime.of(18, 0));
        request.setNumberOfGuests(2);

        Restaurant otherRestaurant = new Restaurant(
                "Other BBQ",
                "Different address",
                "+1 555-2222",
                LocalTime.of(10, 0),
                LocalTime.of(22, 0));
        otherRestaurant.setId(99L);

        when(customerService.findCustomerById(1L)).thenReturn(testCustomer);
        when(restaurantService.findRestaurantById(99L)).thenReturn(otherRestaurant);
        when(restaurantService.findTableById(1L)).thenReturn(testTable);

        // When & Then
        assertThrows(ValidationException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
