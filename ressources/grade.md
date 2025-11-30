# Restaurant Reservation System - Grading Rubric 📊

**Evaluated by:** Professor Baptiste

---

## ⚠️ Automatic Penalties (Applied First)

Check all that apply:

- [ ] ❌ Project not submitted on time → **-15 points**
- [ ] ❌ Did not accept Gitee invitation → **-20 points**
- [ ] ❌ Wrong branch name (not student ID) → **-5 points**
- [ ] ❌ No README.md → **-5 points**
- [ ] ❌ Project doesn't compile → **-20 points**

**Total Penalties:** **\_\_\_** points

---

## 📋 Evaluation (100 Points)

### 1. API Design & Functionality

#### Core Features Implementation

- [ ] Restaurant endpoints work correctly
- [ ] Reservation endpoints work correctly
- [ ] Customer endpoints work correctly
- [ ] Availability checking works

#### Business Rules Enforcement

- [ ] No double-booking (same table, same time)
- [ ] Capacity validation (guests ≤ table capacity)
- [ ] Opening hours respected
- [ ] No past reservations
- [ ] Proper status transitions

#### API Quality

- [ ] RESTful URL structure
- [ ] Correct HTTP methods used
- [ ] Proper HTTP status codes returned
- [ ] JSON request/response format
- [ ] Consistent API design

---

### 2. Clean Code Principles

#### Naming Conventions

- [ ] Classes use PascalCase
- [ ] Methods/variables use camelCase
- [ ] Constants use UPPER_SNAKE_CASE
- [ ] Names are meaningful and descriptive
- [ ] No abbreviations (except common ones like ID, URL)

**Method Length:**

- ≤ 25 lines: ✅ OK (expected for most methods)
- 26-35 lines: ⚠️ Tolerated if justified (complex validation, mapping)
- > 35 lines: ❌ Too long - needs refactoring

**Methods over 35 lines:**

#### Code Quality

- [ ] No code duplication (DRY)
- [ ] No magic numbers (use constants)
- [ ] Proper formatting and indentation
- [ ] Code is readable and self-documenting
- [ ] Single Responsibility followed

#### Encapsulation

- [ ] All fields are private
- [ ] Proper getters/setters only where needed
- [ ] DTOs used (entities not exposed)

---

### 3. Architecture & Design

#### Layer Separation

- [ ] Controller layer (HTTP handling only)
- [ ] Service layer (business logic)
- [ ] Repository layer (data access)
- [ ] Clear boundaries between layers

#### Data Model

- [ ] Entities properly designed
- [ ] Relationships correctly mapped (JPA annotations)
- [ ] Constraints enforced at entity level
- [ ] Appropriate use of enums

#### Exception Handling

- [ ] Custom exceptions for business errors
- [ ] GlobalExceptionHandler implemented
- [ ] Proper HTTP status codes
- [ ] Meaningful error messages
- [ ] No stack traces exposed to client

---

### 4. Git Workflow & Version Control

#### Commit Frequency

- [ ] 10+ commits (Full credit)
- [ ] 7-9 commits (Partial credit)
- [ ] 4-6 commits (Low credit)
- [ ] Less than 4 commits (Minimal credit)

**Number of commits:** **\_\_\_**

#### Commit Quality

- [ ] All commit messages in English
- [ ] Messages are clear and descriptive
- [ ] Commits show logical progression
- [ ] Each commit represents meaningful work

#### Project Evolution

- [ ] Can follow development through commits
- [ ] Incremental progress visible
- [ ] No "final commit with everything"

### 5. Validation & Testing

#### Input Validation

- [ ] All inputs validated
- [ ] Bean Validation annotations used
- [ ] Clear validation error messages
- [ ] Edge cases handled

#### API Tests (api-tests.http)

- [ ] Happy path scenarios included
- [ ] Error scenarios included
- [ ] Business rule violations tested
- [ ] At least 10 test cases

---

## 🌟 Bonus Features (Optional Extra Credit)

**Bonus features implemented:**

- [ ] **Unit Tests (JUnit 5)** (it's important if you want to do java for compagny)

  - Tests for Service layer
  - Mockito used correctly
  - At least 5 meaningful tests
  - Success and error cases covered

- [ ] **JWT Authentication**

  - Registration/Login work
  - Protected endpoints
  - Role-based access

- [ ] **Review System**

  - Reviews can be created
  - Only completed reservations can review
  - Average rating calculated

- [ ] **Advanced Features**
  - Swagger/OpenAPI documentation
  - Smart availability
  - Other creative features

---

## 📝 Final Grade

**Grade:** **\_\_\_** / 100

**Letter Grade:**

- [ ] A (90-100) - Excellent
- [ ] B (80-89) - Good
- [ ] C (70-79) - Satisfactory
- [ ] D (60-69) - Passing
- [ ] F (0-59) - Failing

---

\

**Evaluated by:** Professor Baptiste

---

_This evaluation has been pushed to your repository branch. Review the feedback carefully and use it to improve your skills._
