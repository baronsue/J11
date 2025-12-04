package com.restaurant.service;

import com.restaurant.dto.request.CreateCustomerRequest;
import com.restaurant.dto.response.CustomerResponse;
import com.restaurant.exception.ConflictException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.Customer;
import com.restaurant.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerService.
 */
@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer("John Smith", "john@email.com", "+1 555-0101");
        testCustomer.setId(1L);
    }

    @Test
    @DisplayName("Should create customer successfully when email is unique")
    void shouldCreateCustomerSuccessfully() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("Alice Johnson");
        request.setEmail("alice@email.com");
        request.setPhone("+1 555-0102");

        Customer savedCustomer = new Customer("Alice Johnson", "alice@email.com", "+1 555-0102");
        savedCustomer.setId(2L);

        when(customerRepository.existsByEmail("alice@email.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // When
        CustomerResponse response = customerService.createCustomer(request);

        // Then
        assertNotNull(response);
        assertEquals("Alice Johnson", response.getName());
        assertEquals("alice@email.com", response.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when email already exists")
    void shouldThrowConflictExceptionWhenEmailExists() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("John Copy");
        request.setEmail("john@email.com");
        request.setPhone("+1 555-9999");

        when(customerRepository.existsByEmail("john@email.com")).thenReturn(true);

        // When & Then
        assertThrows(ConflictException.class, () -> customerService.createCustomer(request));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should get customer by ID successfully")
    void shouldGetCustomerByIdSuccessfully() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // When
        CustomerResponse response = customerService.getCustomerById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Smith", response.getName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when customer not found")
    void shouldThrowResourceNotFoundExceptionWhenCustomerNotFound() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(999L));
    }

    @Test
    @DisplayName("Should get all customers successfully")
    void shouldGetAllCustomersSuccessfully() {
        // Given
        Customer customer2 = new Customer("Marie Dupont", "marie@email.com", "+33 6 12 34 56 78");
        customer2.setId(2L);

        when(customerRepository.findAll()).thenReturn(List.of(testCustomer, customer2));

        // When
        List<CustomerResponse> responses = customerService.getAllCustomers();

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("Should get customer by email successfully")
    void shouldGetCustomerByEmailSuccessfully() {
        // Given
        when(customerRepository.findByEmail("john@email.com")).thenReturn(Optional.of(testCustomer));

        // When
        CustomerResponse response = customerService.getCustomerByEmail("john@email.com");

        // Then
        assertNotNull(response);
        assertEquals("john@email.com", response.getEmail());
    }

    @Test
    @DisplayName("Should normalize email to lowercase when creating customer")
    void shouldNormalizeEmailToLowercase() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("Test User");
        request.setEmail("TEST@EMAIL.COM");
        request.setPhone("+1 555-0103");

        Customer savedCustomer = new Customer("Test User", "test@email.com", "+1 555-0103");
        savedCustomer.setId(3L);

        when(customerRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // When
        CustomerResponse response = customerService.createCustomer(request);

        // Then
        assertNotNull(response);
        verify(customerRepository).existsByEmail("test@email.com");
    }
}
