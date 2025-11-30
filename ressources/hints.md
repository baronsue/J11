# Restaurant Reservation System - Technical Hints 💡

**This document provides guidance, NOT solutions.**

You must design and implement everything yourself.

---

## 🧭 Design Guidance

### Entity Relationships to Consider

Think about these questions:

```
Restaurant ──────── ?
     │
     └── has many ──── Tables ──────── ?
                           │
                           └── can have ──── Reservations ──────── ?
                                                  │
                                                  └── belongs to ──── Customer
```

**Questions to answer:**

- What is the cardinality of each relationship?
- Which side owns the relationship in JPA?
- What cascade types make sense?

### Data Model Hints

#### Restaurant might need:

- Basic info (name, address, phone...)
- Operating hours (when does it open/close?)
- Something to hold its tables

#### Table might need:

- Identity (table number?)
- How many people can sit?
- Which restaurant does it belong to?

#### Reservation might need:

- When? (date and time)
- Who? (customer)
- Where? (which table)
- How many people?
- What's the current state? (status)

#### Customer might need:

- Identity
- Contact information
- Something to track their reservations

### Status Flow

Consider a state machine for reservations:

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
│  COMPLETED  │
└─────────────┘
```

**What actions trigger each transition?**

---

## 🌐 API Design Guidance

### REST Conventions

| Action  | HTTP Method | URL Pattern                  |
| ------- | ----------- | ---------------------------- |
| Get all | GET         | `/api/resources`             |
| Get one | GET         | `/api/resources/{id}`        |
| Create  | POST        | `/api/resources`             |
| Update  | PUT         | `/api/resources/{id}`        |
| Delete  | DELETE      | `/api/resources/{id}`        |
| Action  | POST/PUT    | `/api/resources/{id}/action` |

### Nested Resources

For related resources, consider:

```
GET /api/restaurants/{id}/tables        ← Tables of a restaurant
GET /api/restaurants/{id}/availability  ← Available slots
GET /api/customers/{id}/reservations    ← Customer's bookings
```

### Query Parameters

For filtering/searching:

```
GET /api/restaurants?city=Paris
GET /api/restaurants/{id}/availability?date=2025-12-01
GET /api/reservations?status=PENDING
```

---

## ⏰ Handling Time

### Time Representation

Consider using:

- `LocalDate` for dates (e.g., reservation date)
- `LocalTime` for times (e.g., opening/closing hours)
- `LocalDateTime` for precise moments (e.g., exact reservation time)

### Time Slot Approach

One common approach is to divide the day into slots:

- Restaurant open 12:00-22:00
- 2-hour slots: 12:00, 14:00, 16:00, 18:00, 20:00

**But you can design this differently!**

### Checking Availability

Think about:

- What makes two reservations "conflict"?
- Same table + overlapping time = conflict
- How do you query for conflicts in JPA?

---

## 🛡️ Validation Patterns

### Entity-Level Validation

```java
// Example pattern (adapt to your design)
@Entity
public class SomeEntity {
    @NotBlank(message = "Field X is required")
    private String fieldX;

    @Min(value = 1, message = "Field Y must be positive")
    private int fieldY;

    @Email(message = "Invalid email format")
    private String email;
}
```

### Custom Validation

For complex business rules, consider:

1. Validation in the Service layer
2. Custom validator annotations
3. Throwing custom exceptions

### Example Custom Exception Pattern

```java
// Base exception
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

// Specific exceptions
public class ResourceNotFoundException extends BusinessException { ... }
public class ConflictException extends BusinessException { ... }
public class ValidationException extends BusinessException { ... }
```

---

## 🔍 JPA Query Hints

### Finding Conflicts

You might need queries like:

- Find reservations for a specific table on a specific date
- Check if a time slot is already taken
- Find all tables available for a given time

### Custom Query Methods

Spring Data JPA supports:

```java
// By method name (simple cases)
List<Entity> findByFieldAndOtherField(Type field, Type otherField);

// Custom JPQL (complex cases)
@Query("SELECT e FROM Entity e WHERE ...")
List<Entity> customQuery(@Param("param") Type param);
```

### Think About:

- What queries do you need for availability checking?
- How do you find conflicting reservations?

---

## 🎭 DTO Pattern Reminder

### Why DTOs?

1. **Security:** Don't expose internal structure
2. **Flexibility:** Request ≠ Entity ≠ Response
3. **Validation:** Validate input at boundary

### Request vs Response

```
Client Request    →    Controller    →    Service    →    Repository
    DTO                                                        ↓
                                                            Entity
Client Response   ←    Controller    ←    Service    ←    Repository
    DTO
```

### Common DTO Types

- `CreateXxxRequest` - For POST requests
- `UpdateXxxRequest` - For PUT requests
- `XxxResponse` - For responses
- `XxxSummary` - For list views (less detail)

---

## 🚨 Error Handling Pattern

### Global Exception Handler Structure

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SomeException.class)
    public ResponseEntity<ErrorResponse> handleSomeException(SomeException e) {
        // Map exception to appropriate response
        // Choose correct HTTP status
        // Return meaningful error message
    }
}
```

### HTTP Status Guidelines

| Situation               | Status Code               |
| ----------------------- | ------------------------- |
| Validation failed       | 400 Bad Request           |
| Not authenticated       | 401 Unauthorized          |
| Not authorized          | 403 Forbidden             |
| Resource not found      | 404 Not Found             |
| Business rule violation | 409 Conflict              |
| Server error            | 500 Internal Server Error |

---

## 📁 Suggested Package Structure

This is a suggestion, not a requirement:

```
com.restaurant/
├── controller/
│   ├── RestaurantController.java
│   ├── ReservationController.java
│   └── CustomerController.java
├── service/
│   ├── RestaurantService.java
│   ├── ReservationService.java
│   ├── CustomerService.java
│   └── AvailabilityService.java (?)
├── repository/
│   └── ...
├── model/
│   └── ...
├── dto/
│   ├── request/
│   │   └── ...
│   └── response/
│       └── ...
├── exception/
│   └── ...
└── Application.java
```

**Organize in a way that makes sense for YOUR design.**

---

## 🧪 Testing Checklist

### Scenarios to Test:

**Happy Paths:**

- [ ] Create restaurant with tables
- [ ] Register customer
- [ ] Check availability (available)
- [ ] Make reservation successfully
- [ ] View reservation details
- [ ] Cancel reservation
- [ ] View customer history

**Business Rule Violations:**

- [ ] Double-booking same table/time → 409 Conflict
- [ ] Booking in the past → 400 Bad Request
- [ ] Exceeding table capacity → 400 Bad Request
- [ ] Booking outside hours → 400 Bad Request
- [ ] Cancel completed reservation → 409 Conflict

**Validation Errors:**

- [ ] Missing required fields → 400 Bad Request
- [ ] Invalid email format → 400 Bad Request
- [ ] Invalid date format → 400 Bad Request
- [ ] Negative values → 400 Bad Request

**Not Found:**

- [ ] Restaurant not found → 404
- [ ] Reservation not found → 404
- [ ] Customer not found → 404

---

## 💾 Sample application.properties

```properties
# Database
spring.datasource.url=jdbc:h2:mem:restaurantdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Date format (optional - for consistent JSON)
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=Asia/Shanghai
```

---

## 🤔 Questions to Ask Yourself

Before coding, answer these:

1. **What are my entities and their relationships?**
2. **What fields does each entity need?**
3. **What are my API endpoints?**
4. **How do I represent time slots?**
5. **How do I check for booking conflicts?**
6. **What exceptions can occur?**
7. **What status transitions are valid?**

**If you can't answer these on paper, you're not ready to code.**

---

## ⚠️ Common Mistakes to Avoid

1. ❌ Starting to code without a plan
2. ❌ Exposing entities directly in API
3. ❌ Ignoring validation
4. ❌ No custom exceptions (just generic errors)
5. ❌ Business logic in controllers
6. ❌ Magic numbers everywhere
7. ❌ Methods longer than 25 lines (35 max if justified)
8. ❌ No error handling for edge cases
9. ❌ Committing broken code
10. ❌ Waiting until the last day

---

**This document gives you direction. The implementation is 100% your responsibility.**

**Design first, code second! 📝**

---

## 🧪 Bonus: Unit Testing Hints (JUnit 5)

If you want to implement the testing bonus, here's some guidance:

### Dependencies to Add (pom.xml)

Spring Boot Starter Test is usually included by default. If not:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Test Structure

```
src/test/java/com/restaurant/
├── service/
│   ├── ReservationServiceTest.java
│   └── RestaurantServiceTest.java
└── controller/
    └── ReservationControllerTest.java (optional)
```

### What to Test

Focus on **Service layer** tests:

- Business rule validations (double-booking, capacity, etc.)
- Success scenarios
- Exception throwing

### Example Pattern (adapt to your code)

```java
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldThrowExceptionWhenTableAlreadyBooked() {
        // Given: setup your test data
        // When: call the method
        // Then: verify the result or exception
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        // Given - When - Then
    }
}
```

### Useful Annotations

- `@Test` - Marks a test method
- `@Mock` - Creates a mock object
- `@InjectMocks` - Injects mocks into the tested class
- `@BeforeEach` - Runs before each test

### Mockito Basics

```java
// Make a mock return something
when(repository.findById(1L)).thenReturn(Optional.of(entity));

// Verify a method was called
verify(repository).save(any(Reservation.class));

// Assert results
assertEquals(expected, actual);
assertThrows(SomeException.class, () -> service.method());
```

**Remember:** Testing is about proving your code works correctly. Focus on testing your business rules!
