package com.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration class.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Reservation API")
                        .version("1.0.0")
                        .description("""
                                A comprehensive Restaurant Reservation System API built with Spring Boot.
                                
                                ## Features
                                - Restaurant Management: View restaurants, tables, and availability
                                - Reservation Management: Create, confirm, cancel, and complete reservations
                                - Customer Management: Register and manage customer profiles
                                - Review System: Leave and view restaurant reviews
                                
                                ## Business Rules
                                - No double-booking for the same table and time slot
                                - Guests cannot exceed table capacity
                                - Reservations only within opening hours
                                - Cannot book past dates/times
                                """)
                        .contact(new Contact()
                                .name("Student Developer")
                                .email("student@email.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server")
                ));
    }
}
