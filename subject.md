# Restaurant Reservation System - Final Exam 🍽️

**Course:** Java Programming (OOP) - Spring Boot  
**Instructor:** Prof. Baptiste Dupuis  
**DEADLINE:** Sunday 7 decdember 11:42 pm

[INVITATION TO GITEE REPOSITORY] - Link available Monday 1 December

---

## 🎯 Project Overview

You are building a **Restaurant Reservation API** using Spring Boot.

This is a **real-world scenario**: customers want to browse restaurants, check availability, and make reservations online. Restaurant owners want to manage their tables and track bookings.

**You are NOT building a toy project.** This is the kind of system that actually exists in production at companies like OpenTable, TheFork, or Resy.

---

## 🍴 The Business Problem

Imagine you're a software developer hired by a restaurant group. They need a system that can:

1. **Manage their restaurants** - Each restaurant has multiple tables with different capacities
2. **Handle reservations** - Customers book tables for specific dates and times
3. **Track availability** - No double-booking, respect opening hours
4. **Manage customers** - Remember who has booked before

**Your API will power their mobile app and website.**

---

## 📋 Functional Requirements

### Core Features (Required)

Your API must support:

#### 1. Restaurant Management

- View all restaurants
- View restaurant details (including tables)
- Check restaurant availability for a specific date

#### 2. Reservation Management

- Create a new reservation
- View reservation details
- Cancel a reservation
- View customer's reservation history

#### 3. Customer Management

- Register a new customer
- View customer profile

### Business Rules (Must be enforced)

1. **No double-booking:** A table cannot be reserved twice for the same time slot
2. **Capacity check:** Number of guests must not exceed table capacity
3. **Opening hours:** Reservations only during restaurant opening hours
4. **No past reservations:** Cannot book for dates/times in the past
5. **Cancellation rules:** Can only cancel PENDING or CONFIRMED reservations

---

## 🎨 Your Design Decisions

**This project is intentionally less guided than the Store Inventory project.**

You must make your own architectural decisions:

### Questions you must answer:

1. **What entities do you need?**

   - Think about: Restaurant, Table, Reservation, Customer, ...
   - What are the relationships between them?

2. **What fields does each entity need?**

   - What information is essential?
   - What constraints should you enforce?

3. **What endpoints will you expose?**

   - What HTTP methods?
   - What URL structure?
   - What request/response formats?

4. **How do you check availability?**

   - How do you prevent double-booking?
   - What about time slots?

5. **What status flow makes sense for reservations?**
   - What statuses are possible?
   - What transitions are allowed?

### Hints (not requirements):

- A restaurant has multiple tables
- A table belongs to one restaurant
- A reservation is for one table
- A customer can have multiple reservations
- Time slots might be useful (e.g., 2-hour blocks)
- Think about what makes a "conflict" for booking

---

## 🏗️ Technical Requirements

### Must Use:

- **Spring Boot 3.x** with Java 21
- **Spring Data JPA** for persistence
- **H2 Database** (in-memory for development)
- **Spring Validation** for input validation
- **Proper exception handling** with @RestControllerAdvice

### Project Structure:

You decide your package organization, but it must follow Clean Code principles:

- Clear separation of concerns
- Controller → Service → Repository layers
- DTO pattern (never expose entities directly)
- Custom exceptions for business errors

### API Design:

- RESTful conventions (proper HTTP methods and status codes)
- JSON request/response format
- Meaningful error messages
- Proper validation feedback

---

## 📏 Clean Code Requirements (Strictly Enforced)

### Naming Conventions

| Element           | Convention       | Example                  |
| ----------------- | ---------------- | ------------------------ |
| Classes           | PascalCase       | `ReservationService`     |
| Methods/Variables | camelCase        | `findByRestaurantId`     |
| Constants         | UPPER_SNAKE_CASE | `MAX_GUESTS_PER_TABLE`   |
| Packages          | lowercase        | `com.restaurant.service` |

### Code Quality Rules

1. **Methods should be under 25 lines** - Most methods must be ≤25 lines. Exceptionally, up to 35 lines is tolerated if justified (complex validation, mapping logic). Beyond 35 lines = refactor required.
2. **No magic numbers** - Use constants
3. **No code duplication** - DRY principle
4. **Meaningful names** - `calculateTotalAvailableSlots()` not `calc()`
5. **Single Responsibility** - One class, one purpose
6. **Proper encapsulation** - All fields private
7. **Immutable where possible** - Especially DTOs

### Exception Handling

- Custom exceptions for business errors
- Global exception handler
- Proper HTTP status codes:
  - `200 OK` - Success
  - `201 Created` - Resource created
  - `400 Bad Request` - Validation error
  - `404 Not Found` - Resource not found
  - `409 Conflict` - Business rule violation (e.g., double-booking)

### Validation

- Validate ALL inputs
- Clear validation messages
- Use Bean Validation annotations (`@NotNull`, `@NotBlank`, `@Min`, `@Email`, etc.)

---

## 🧪 Testing Your API

Create a file `api-tests.http` (for REST Client in VSCode) demonstrating:

1. **Happy path scenarios:**

   - Create a reservation successfully
   - View restaurant availability
   - Cancel a reservation

2. **Error scenarios:**
   - Try to double-book a table
   - Try to book in the past
   - Try to book outside opening hours
   - Try to exceed table capacity
   - Invalid input validation

Your tests should prove your business rules work correctly.

---

## 🎁 Bonus Features (Optional)

Implementing bonus features can earn extra points:

### Bonus 1: Unit Tests (JUnit 5)

- Write unit tests for your Service layer
- Use Mockito to mock repositories
- Minimum 5 meaningful tests
- Test both success cases and error cases

### Bonus 2: JWT Authentication

- User registration and login
- Protected endpoints (only authenticated users can make reservations)
- Role-based access (ADMIN vs USER)

### Bonus 3: Review System

- Customers can leave reviews for restaurants
- Rating (1-5 stars) + comment
- Only customers who completed a reservation can review

### Bonus 4: Advanced Features

- Smart time slot suggestions
- Automatic table assignment based on party size
- Swagger/OpenAPI documentation

---

## 📊 What Success Looks Like

### Minimum (Passing - 60%):

- API runs without errors
- Basic CRUD operations work
- At least 3 business rules enforced
- Code compiles and structure is logical

### Good (70-80%):

- All required features work
- All business rules enforced
- Clean Code principles followed
- Good error handling
- Clear API design

### Excellent (90-100%):

- Everything above
- Exceptional code quality
- Creative solutions to complex problems
- Comprehensive test scenarios
- Professional-grade implementation

---

## ⚠️ Important Notes

1. **This is an individual project** - Collaboration is not allowed
2. **Plagiarism = 0** - Including from AI without understanding
3. **Commits matter** - Show your development process
4. **Clean Code is not optional** - It's a core requirement

---

## 🚀 Getting Started

1. Accept the Gitee invitation (within 48 hours)
2. Clone the repository
3. Create your branch (your student ID)
4. Design your data model first (on paper!)
5. Plan your API endpoints
6. Start coding incrementally
7. Commit often with meaningful messages
8. Test thoroughly
9. Document your decisions in README.md

---

**Remember:** This exam tests your ability to design and implement a professional system, not just follow instructions. Show us what you've learned! 💪

---

**Good luck! 🍀**
