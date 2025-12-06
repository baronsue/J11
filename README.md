# Restaurant Reservation API 🍽️

## Author

- **Name:** Baron
- **Student ID:** 2023905332

## Project Description

A comprehensive Restaurant Reservation System API built with Spring Boot. This system allows customers to browse restaurants, check table availability, make reservations online, and leave reviews. Restaurant owners can manage their tables and track bookings efficiently.

### Key Features

- ✅ **Restaurant Management:** View all restaurants, restaurant details with tables, check availability
- ✅ **Reservation Management:** Create, view, confirm, cancel, and complete reservations
- ✅ **Customer Management:** Register customers, view profiles, track reservation history
- ✅ **Review System:** Leave reviews for completed reservations, view ratings (Bonus)
- ✅ **Business Rules Enforcement:** Double-booking prevention, capacity validation, opening hours check
- ✅ **Swagger/OpenAPI Documentation:** Interactive API documentation (Bonus)
- ✅ **Unit Tests:** Comprehensive JUnit 5 tests with Mockito (Bonus)

## Technology Stack

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Spring Validation**
- **SpringDoc OpenAPI** (Swagger UI)
- **JUnit 5 + Mockito** (Testing)
- **Maven**

## How to Run

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Steps

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd java-final-project
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the API at: `http://localhost:8080`

5. **Swagger UI Documentation:** `http://localhost:8080/swagger-ui.html`

6. **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

7. **H2 Console** (for database inspection):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:restaurantdb`
   - Username: `sa`
   - Password: (empty)

## Running Tests

```bash
# Run all unit tests
mvn test

# Run tests with coverage report
mvn test jacoco:report
```

## API Endpoints

### Restaurants

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/restaurants` | Get all restaurants |
| GET | `/api/restaurants/{id}` | Get restaurant details with tables |
| GET | `/api/restaurants/{id}/tables` | Get restaurant tables |
| GET | `/api/restaurants/{id}/availability?date=YYYY-MM-DD&guests=N` | Check availability |
| GET | `/api/restaurants/{id}/reservations` | Get restaurant's reservations |

### Reservations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reservations` | Create new reservation |
| GET | `/api/reservations/{id}` | Get reservation details |
| PUT | `/api/reservations/{id}/confirm` | Confirm reservation |
| PUT | `/api/reservations/{id}/cancel` | Cancel reservation |
| PUT | `/api/reservations/{id}/complete` | Mark reservation as completed |

### Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customers` | Register new customer |
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer profile |
| GET | `/api/customers/{id}/reservations` | Get customer's reservation history |

### Reviews (Bonus Feature)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reviews` | Create a review (completed reservations only) |
| GET | `/api/reviews/{id}` | Get review by ID |
| GET | `/api/reviews/restaurant/{restaurantId}` | Get all reviews for a restaurant |
| GET | `/api/reviews/restaurant/{restaurantId}/rating` | Get restaurant average rating |
| GET | `/api/reviews/customer/{customerId}` | Get customer's reviews |

## Request/Response Examples

### Create Reservation

**Request:**
```json
POST /api/reservations
{
    "restaurantId": 1,
    "customerId": 1,
    "reservationDate": "2025-12-25",
    "reservationTime": "19:00:00",
    "numberOfGuests": 4
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "reservationDate": "2025-12-25",
    "reservationTime": "19:00:00",
    "endTime": "21:00:00",
    "numberOfGuests": 4,
    "status": "PENDING",
    "table": {
        "id": 2,
        "tableNumber": "T02",
        "capacity": 4
    },
    "restaurantName": "Le Petit Bistro",
    "customer": {
        "id": 1,
        "name": "John Smith",
        "phone": "+1 555-0101"
    }
}
```

### Create Review (Bonus)

**Request:**
```json
POST /api/reviews
{
    "reservationId": 1,
    "rating": 5,
    "comment": "Excellent food and service! Highly recommended!"
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "rating": 5,
    "comment": "Excellent food and service! Highly recommended!",
    "createdAt": "2025-12-01T12:00:00",
    "customerName": "John Smith",
    "restaurantName": "Le Petit Bistro"
}
```

## Business Rules Enforced

1. **No Double-Booking:** A table cannot be reserved twice for the same 2-hour time slot
2. **Capacity Validation:** Number of guests must not exceed table capacity
3. **Opening Hours:** Reservations only allowed during restaurant operating hours
4. **No Past Reservations:** Cannot book for dates/times in the past
5. **Status Transitions:**
   - PENDING → CONFIRMED or CANCELLED
   - CONFIRMED → COMPLETED or CANCELLED
   - CANCELLED and COMPLETED are final states
6. **Review Rules:** Only completed reservations can be reviewed (one review per reservation)

## Reservation Status Flow

```
        ┌─────────────┐
        │   PENDING   │
        └──────┬──────┘
               │
       ┌───────┴───────┐
       ▼               ▼
┌─────────────┐  ┌─────────────┐
│  CONFIRMED  │  │  CANCELLED  │
└──────┬──────┘  └─────────────┘
       │
       ▼
┌─────────────┐
│  COMPLETED  │ ──► Can leave review
└─────────────┘
```

## Design Decisions

### 1. Entity Design

- **Restaurant:** Contains basic info and operating hours
- **RestaurantTable:** Named to avoid SQL keyword conflict; belongs to one restaurant
- **Reservation:** Connects customer to a specific table at a specific time
- **Customer:** Stores contact information for reservation management
- **Review:** Linked to completed reservations for authentic feedback

### 2. Time Slot Approach

- Each reservation occupies a 2-hour slot
- Availability check returns all available time slots with suitable tables
- Time slots are generated based on restaurant opening/closing hours

### 3. Automatic Table Assignment

- When `tableId` is not specified in the request, the system automatically finds a suitable available table
- Tables are matched based on capacity and availability

### 4. DTO Pattern

- Entities are never exposed directly in API responses
- Separate DTOs for requests and responses
- Clear separation between internal model and API contract

## Error Handling

| HTTP Status | Error Type | Example |
|-------------|------------|---------|
| 400 Bad Request | Validation errors, invalid input | Missing required fields, invalid date |
| 404 Not Found | Resource not found | Restaurant/Customer/Reservation not found |
| 409 Conflict | Business rule violation | Double booking, duplicate review |
| 500 Internal Server Error | Unexpected errors | Server-side exceptions |

## Project Structure

```
src/main/java/com/restaurant/
├── ReservationApiApplication.java
├── config/
│   ├── DataInitializer.java          # Sample data initialization
│   └── OpenApiConfig.java            # Swagger configuration
├── controller/
│   ├── CustomerController.java
│   ├── ReservationController.java
│   ├── RestaurantController.java
│   └── ReviewController.java         # Bonus feature
├── dto/
│   ├── request/
│   │   ├── AvailabilityRequest.java
│   │   ├── CreateCustomerRequest.java
│   │   ├── CreateReservationRequest.java
│   │   └── CreateReviewRequest.java  # Bonus feature
│   └── response/
│       ├── AvailabilityResponse.java
│       ├── CustomerResponse.java
│       ├── ErrorResponse.java
│       ├── ReservationResponse.java
│       ├── RestaurantResponse.java
│       ├── RestaurantRatingResponse.java  # Bonus feature
│       ├── ReviewResponse.java            # Bonus feature
│       └── TableResponse.java
├── exception/
│   ├── BusinessException.java
│   ├── ConflictException.java
│   ├── GlobalExceptionHandler.java
│   ├── InvalidOperationException.java
│   ├── ResourceNotFoundException.java
│   └── ValidationException.java
├── model/
│   ├── Customer.java
│   ├── Reservation.java
│   ├── ReservationStatus.java
│   ├── Restaurant.java
│   ├── RestaurantTable.java
│   └── Review.java                   # Bonus feature
├── repository/
│   ├── CustomerRepository.java
│   ├── ReservationRepository.java
│   ├── RestaurantRepository.java
│   ├── RestaurantTableRepository.java
│   └── ReviewRepository.java         # Bonus feature
└── service/
    ├── CustomerService.java
    ├── ReservationService.java
    ├── RestaurantService.java
    └── ReviewService.java            # Bonus feature

src/test/java/com/restaurant/
└── service/
    ├── CustomerServiceTest.java      # 7 test cases
    ├── ReservationServiceTest.java   # 10 test cases
    └── RestaurantServiceTest.java    # 8 test cases
```

## Bonus Features Implemented

### ✅ 1. Unit Tests (JUnit 5)
- **25+ unit tests** covering Service layer
- Uses Mockito for mocking dependencies
- Tests both success cases and error cases
- Coverage includes:
  - Reservation creation, cancellation, status transitions
  - Double-booking prevention
  - Customer registration and retrieval
  - Restaurant availability checking

### ✅ 2. Review System
- Customers can leave reviews for restaurants
- Rating system (1-5 stars) with optional comment
- Only customers who **completed** a reservation can review
- One review per reservation
- Average rating calculation per restaurant

### ✅ 3. Swagger/OpenAPI Documentation
- Interactive API documentation at `/swagger-ui.html`
- Complete endpoint descriptions
- Request/response examples
- API version information

## Testing

Use the provided `api-tests.http` file with VS Code REST Client extension or IntelliJ HTTP Client to test all API endpoints.

The test file includes **40+ test cases**:
- Happy path scenarios for all features
- Error scenarios for business rule violations
- Validation error tests
- Status transition tests
- Review system tests

## Sample Data

The application initializes with sample data on startup:

### Restaurants
1. **Le Petit Bistro** (Paris) - Opens 11:00-23:00, Tables: 2, 4, 6 capacity
2. **Dragon Palace** (Shanghai) - Opens 10:00-22:00, Tables: 2, 4, 8, 10 capacity
3. **Bella Italia** (Milan) - Opens 12:00-23:00, Tables: 2, 2, 4, 4, 6 capacity

### Customers
1. John Smith (john.smith@email.com)
2. Marie Dupont (marie.dupont@email.com)
3. Zhang Wei (zhang.wei@email.com)

## Clean Code Principles Applied

- ✅ **Single Responsibility:** Each class has one clear purpose
- ✅ **Meaningful Names:** Clear, descriptive variable and method names
- ✅ **Methods under 25 lines:** Most methods are concise and focused
- ✅ **No Magic Numbers:** Constants used for all numeric values
- ✅ **DRY Principle:** No code duplication
- ✅ **Proper Encapsulation:** All fields private, access via getters/setters
- ✅ **Input Validation:** All inputs validated with clear error messages
- ✅ **Exception Handling:** Custom exceptions with appropriate HTTP status codes

## Challenges & Solutions

### Challenge 1: Preventing Double Booking
**Solution:** Implemented time overlap detection in the service layer. Each reservation occupies a 2-hour slot, and new reservations are checked against existing ones for the same table.

### Challenge 2: Flexible Table Assignment
**Solution:** Added optional `tableId` parameter. When not provided, the system automatically finds the best available table based on capacity and availability.

### Challenge 3: Clean Status Transitions
**Solution:** Implemented validation methods for each status transition to ensure only valid state changes are allowed.

### Challenge 4: Review Authentication
**Solution:** Reviews are linked to completed reservations, ensuring only genuine customers who dined can leave reviews.

---

**Swagger UI:** http://localhost:8080/swagger-ui.html
