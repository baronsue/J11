package com.restaurant.service;

import com.restaurant.dto.request.CreateCustomerRequest;
import com.restaurant.dto.response.CustomerResponse;
import com.restaurant.exception.ConflictException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.model.Customer;
import com.restaurant.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Customer service class, handles customer-related business logic.
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Register new customer.
     */
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        validateEmailNotExists(request.getEmail());

        Customer customer = new Customer(
                request.getName().trim(),
                request.getEmail().toLowerCase().trim(),
                request.getPhone().trim()
        );

        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponse.fromEntity(savedCustomer);
    }

    /**
     * Get customer information by ID.
     */
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return CustomerResponse.fromEntity(customer);
    }

    /**
     * Get all customers list.
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponse::fromEntity)
                .toList();
    }

    /**
     * Get customer information by email.
     */
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
        return CustomerResponse.fromEntity(customer);
    }

    /**
     * Internal method: Find customer entity by ID.
     */
    @SuppressWarnings("null")
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
    }

    private void validateEmailNotExists(String email) {
        if (customerRepository.existsByEmail(email.toLowerCase().trim())) {
            throw new ConflictException("Customer with email '" + email + "' already exists");
        }
    }
}

