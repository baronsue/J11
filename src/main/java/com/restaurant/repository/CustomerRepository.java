package com.restaurant.repository;

import com.restaurant.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Customer data access interface.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find customer by email.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Check if customer with email exists.
     */
    boolean existsByEmail(String email);
}

