package com.restaurant.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Restaurant entity, contains restaurant basic information and opening hours.
 */
@Entity
@Table(name = "restaurants")
public class Restaurant {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_ADDRESS_LENGTH = 255;
    private static final int MAX_PHONE_LENGTH = 20;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Restaurant name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = MAX_ADDRESS_LENGTH, message = "Address must not exceed 255 characters")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "Phone number is required")
    @Size(max = MAX_PHONE_LENGTH, message = "Phone number must not exceed 20 characters")
    @Column(nullable = false)
    private String phone;

    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Opening time is required")
    @Column(nullable = false)
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    @Column(nullable = false)
    private LocalTime closingTime;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantTable> tables = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(String name, String address, String phone,
            LocalTime openingTime, LocalTime closingTime) {
        validateName(name);
        validateAddress(address);
        validatePhone(phone);
        validateOpeningTime(openingTime);
        validateClosingTime(closingTime);
        validateBusinessHours(openingTime, closingTime);

        this.name = name;
        this.address = address;
        this.phone = phone;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        validateOpeningTime(openingTime);
        validateBusinessHours(openingTime, this.closingTime);
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        validateClosingTime(closingTime);
        validateBusinessHours(this.openingTime, closingTime);
        this.closingTime = closingTime;
    }

    public List<RestaurantTable> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public void setTables(List<RestaurantTable> tables) {
        this.tables = tables != null ? tables : new ArrayList<>();
    }

    public void addTable(RestaurantTable table) {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
        tables.add(table);
        table.setRestaurant(this);
    }

    public void removeTable(RestaurantTable table) {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
        tables.remove(table);
        table.setRestaurant(null);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
    }

    private void validateOpeningTime(LocalTime openingTime) {
        if (openingTime == null) {
            throw new IllegalArgumentException("Opening time cannot be null");
        }
    }

    private void validateClosingTime(LocalTime closingTime) {
        if (closingTime == null) {
            throw new IllegalArgumentException("Closing time cannot be null");
        }
    }

    private void validateBusinessHours(LocalTime openingTime, LocalTime closingTime) {
        if (openingTime != null && closingTime != null && !openingTime.isBefore(closingTime)) {
            throw new IllegalArgumentException("Opening time must be before closing time");
        }
    }
}
