package com.restaurant.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RestaurantTable entity, represents a table in a restaurant.
 * Use RestaurantTable to avoid conflict with SQL keyword Table.
 */
@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {

    private static final int MINIMUM_CAPACITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Table number is required")
    @Column(nullable = false)
    private String tableNumber;

    @NotNull(message = "Capacity is required")
    @Min(value = MINIMUM_CAPACITY, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public RestaurantTable() {
    }

    public RestaurantTable(String tableNumber, Integer capacity) {
        validateTableNumber(tableNumber);
        validateCapacity(capacity);

        this.tableNumber = tableNumber.trim();
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        validateTableNumber(tableNumber);
        this.tableNumber = tableNumber.trim();
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations != null ? reservations : new ArrayList<>();
    }

    private void validateTableNumber(String tableNumber) {
        if (tableNumber == null || tableNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Table number cannot be empty");
        }
    }

    private void validateCapacity(Integer capacity) {
        if (capacity == null || capacity < MINIMUM_CAPACITY) {
            throw new IllegalArgumentException(
                    "Capacity must be at least " + MINIMUM_CAPACITY);
        }
    }
}
