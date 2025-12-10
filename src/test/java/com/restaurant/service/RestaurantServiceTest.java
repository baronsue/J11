package com.restaurant.service;

import com.restaurant.dto.response.RestaurantResponse;
import com.restaurant.dto.response.TableResponse;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.Restaurant;
import com.restaurant.model.RestaurantTable;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.RestaurantTableRepository;
import com.restaurant.security.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RestaurantService.
 */
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant(
                "Test Restaurant",
                "123 Test Street",
                "+1 555-0000",
                LocalTime.of(10, 0),
                LocalTime.of(22, 0));
        testRestaurant.setId(1L);
        testRestaurant.setDescription("A test restaurant");

        RestaurantTable table1 = new RestaurantTable("T01", 2);
        table1.setId(1L);

        RestaurantTable table2 = new RestaurantTable("T02", 4);
        table2.setId(2L);
        testRestaurant.addTable(table1);
        testRestaurant.addTable(table2);
    }

    @Test
    @DisplayName("Should get all restaurants successfully")
    void shouldGetAllRestaurantsSuccessfully() {
        // Given
        when(restaurantRepository.findAll()).thenReturn(List.of(testRestaurant));

        // When
        List<RestaurantResponse> responses = restaurantService.getAllRestaurants();

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test Restaurant", responses.get(0).getName());
    }

    @Test
    @DisplayName("Should get restaurant by ID with tables")
    void shouldGetRestaurantByIdWithTables() {
        // Given
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));

        // When
        RestaurantResponse response = restaurantService.getRestaurantById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Restaurant", response.getName());
        assertNotNull(response.getTables());
        assertEquals(2, response.getTables().size());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when restaurant not found")
    void shouldThrowResourceNotFoundExceptionWhenRestaurantNotFound() {
        // Given
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(999L));
    }

    @Test
    @DisplayName("Should get tables by restaurant ID successfully")
    void shouldGetTablesByRestaurantIdSuccessfully() {
        // Given
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(tableRepository.findByRestaurantId(1L)).thenReturn(testRestaurant.getTables());

        // When
        List<TableResponse> responses = restaurantService.getTablesByRestaurantId(1L);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("Should find table by ID successfully")
    void shouldFindTableByIdSuccessfully() {
        // Given
        RestaurantTable table = testRestaurant.getTables().get(0);
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));

        // When
        RestaurantTable result = restaurantService.findTableById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("T01", result.getTableNumber());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when table not found")
    void shouldThrowResourceNotFoundExceptionWhenTableNotFound() {
        // Given
        when(tableRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> restaurantService.findTableById(999L));
    }
}
