package com.restaurant.dto.response;

import com.restaurant.model.Restaurant;

import java.time.LocalTime;
import java.util.List;

/**
 * Restaurant response DTO.
 */
public class RestaurantResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<TableResponse> tables;

    public RestaurantResponse() {
    }

    public static RestaurantResponse fromEntity(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setAddress(restaurant.getAddress());
        response.setPhone(restaurant.getPhone());
        response.setDescription(restaurant.getDescription());
        response.setOpeningTime(restaurant.getOpeningTime());
        response.setClosingTime(restaurant.getClosingTime());
        response.setTables(
                restaurant.getTables().stream()
                        .map(TableResponse::fromEntity)
                        .toList()
        );
        return response;
    }

    public static RestaurantResponse fromEntitySummary(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setAddress(restaurant.getAddress());
        response.setPhone(restaurant.getPhone());
        response.setDescription(restaurant.getDescription());
        response.setOpeningTime(restaurant.getOpeningTime());
        response.setClosingTime(restaurant.getClosingTime());
        return response;
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
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public List<TableResponse> getTables() {
        return tables;
    }

    public void setTables(List<TableResponse> tables) {
        this.tables = tables;
    }
}

