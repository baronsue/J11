package com.restaurant.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Create customer request DTO.
 */
public class CreateCustomerRequest {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final String PHONE_PATTERN = "^[0-9+\\-\\s()]{8,20}$";

    @NotBlank(message = "Customer name is required")
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, 
          message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = PHONE_PATTERN, message = "Invalid phone number format")
    private String phone;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
