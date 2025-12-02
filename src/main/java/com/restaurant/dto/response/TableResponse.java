package com.restaurant.dto.response;

import com.restaurant.model.RestaurantTable;

/**
 * Table response DTO.
 */
public class TableResponse {

    private Long id;
    private String tableNumber;
    private Integer capacity;

    public TableResponse() {
    }

    public static TableResponse fromEntity(RestaurantTable table) {
        TableResponse response = new TableResponse();
        response.setId(table.getId());
        response.setTableNumber(table.getTableNumber());
        response.setCapacity(table.getCapacity());
        return response;
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
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}

