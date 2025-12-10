# 🚀 Dumpling House — Professional Evolution Guide

**Transform your exam project into a portfolio-ready application**

---

## 🎉 Congratulations — You've Already Done the Hard Part!

By completing the Restaurant Reservation API exam, you have already built the **core foundation** of a professional application. You have:

- ✅ Designed a real-world data model
- ✅ Implemented business logic with Spring Boot
- ✅ Created RESTful API endpoints
- ✅ Handled validation and exceptions
- ✅ Applied Clean Code principles

**This is the hardest part of any project — starting from zero.**

Now, with some additional effort, you can transform this exam project into a **100% professional application** that will impress employers and stand out in your portfolio.

### Why This Matters

Most university graduates show employers:

- Simple CRUD applications
- Tutorial copy-paste projects
- Incomplete or broken demos

**You will show them:**

- A complete full-stack application
- Production-ready architecture
- Professional documentation and deployment

**This puts you ahead of 90% of candidates.** Employers will see that you can build and ship real software — not just follow tutorials.

---

## Overview

This guide provides a step-by-step roadmap to evolve your Restaurant Reservation API into a professional, production-grade application. Each phase builds upon the previous one, so complete them in order.

By the end, you will have a project that demonstrates real-world skills to employers.

```
Phase 1: Foundation         ⭐       → Professional backend basics
Phase 2: Database           ⭐       → Production-ready persistence
Phase 3: Security           ⭐⭐     → Enterprise-grade authentication
Phase 4: Frontend           ⭐⭐     → Vue.js or React customer interface
Phase 5: Analytics          ⭐       → Metabase dashboards with Docker
Phase 6: DevOps             ⭐⭐     → Containerization & deployment
Phase 7: Cloud Deployment   ⭐⭐     → Free hosting with live demo URL
Phase 8: Documentation      ⭐       → Portfolio presentation
Phase 9: Future Vision      ⭐⭐     → WeChat Mini Program (documentation)
Phase 10: Adaptability      ⭐⭐     → Swap technologies to learn more
```

### 💡 Flexibility Note

While phases are numbered sequentially, some can be done in parallel or reordered:

- **Phases 1-2** must be done first (backend foundation)
- **Phase 3** (Security) should come before Phase 4 (Frontend)
- **Phases 5-6-7** can be done in any order after Phase 4
- **Phase 8** (Documentation) should be ongoing throughout
- **Phase 9** (WeChat) can be done anytime as it's documentation only

**Recommendation:** Complete Phases 1-4 first, then choose your path based on interest.

---

## 🏢 Project Context: Choose Your Restaurant Theme

You are building a reservation system for a **restaurant chain** with locations in multiple Chinese cities.

### Make It Personal!

Choose a theme that reflects your personality or interests. This makes your portfolio project unique and memorable.

**Example themes:**

| Theme        | Name Example                | Cities                       |
| ------------ | --------------------------- | ---------------------------- |
| 🥟 Dumplings | Dumpling House (饺子坊)     | Xi'an, Beijing, Chengdu      |
| 🍜 Noodles   | Noodle Kingdom (面王国)     | Lanzhou, Chongqing, Shanghai |
| 🍔 Burgers   | Dragon Burger (龙堡)        | Beijing, Shenzhen, Guangzhou |
| 🍣 Sushi     | Sakura Sushi (樱花寿司)     | Shanghai, Hangzhou, Suzhou   |
| 🥘 Hot Pot   | Fire Mountain (火山火锅)    | Chengdu, Chongqing, Xi'an    |
| 🍕 Pizza     | Great Wall Pizza (长城披萨) | Beijing, Tianjin, Xi'an      |

**Pick one theme and use it consistently throughout your project.** This document uses "Dumpling House" as an example.

### User Roles

| Role        | Chinese  | Scope          | Responsibilities                              |
| ----------- | -------- | -------------- | --------------------------------------------- |
| Super Admin | 总管理员 | Entire chain   | Manage all restaurants, view global analytics |
| Manager     | 店长     | One restaurant | Manage tables, staff, view local analytics    |
| Staff       | 服务员   | One restaurant | Confirm arrivals, complete reservations       |
| Customer    | 顾客     | All cities     | Make and manage reservations                  |

---

## 🛠️ Prerequisites

Before starting the evolution phases, make sure you have:

**From your exam project:**

- ✅ Working Spring Boot application
- ✅ Basic CRUD operations for restaurants, tables, reservations
- ✅ At least some business rules implemented

**Development environment:**

- ✅ Java 21 installed
- ✅ Maven or Gradle configured
- ✅ Git for version control
- ✅ IDE (IntelliJ IDEA recommended, or VSCode)
- ✅ Docker Desktop installed (for Phase 6+)
- ✅ Node.js 18+ (for Phase 4 - Frontend)

**Accounts you will need:**

- GitHub or Gitee account (you already have this)
- Railway account (free) — for Phase 7
- Vercel account (free) — for Phase 7

---

# Phase 1: Foundation Enhancement ⚡

**Goal:** Improve your exam code to professional standards

**Difficulty:** ⭐ (Beginner-friendly)

---

## 1.1 Code Quality Improvements

### Refactor for Clean Architecture

Ensure clear separation between layers:

```
com.dumplinghouse/
├── controller/          ← HTTP handling only
├── service/             ← Business logic
├── repository/          ← Data access
├── model/
│   ├── entity/          ← JPA entities
│   └── enums/           ← Status enums
├── dto/
│   ├── request/         ← Input validation
│   └── response/        ← API responses
├── exception/           ← Custom exceptions
├── config/              ← Configuration classes
└── util/                ← Helper utilities
```

### Add Comprehensive Validation

Every input must be validated with clear error messages:

- Use Bean Validation annotations
- Create custom validators for complex rules
- Return user-friendly error messages

### Improve Exception Handling

Create a hierarchy of business exceptions:

- ResourceNotFoundException (404)
- ValidationException (400)
- ConflictException (409) — for double-booking
- UnauthorizedException (401)
- ForbiddenException (403)

---

## 1.2 Add Multi-Restaurant Support

### Update Data Model

Your system must support multiple restaurants in different cities:

```
Brand (Dumpling House)
    └── Restaurant (Xi'an, Beijing, Chengdu...)
            └── Table (1, 2, 3...)
                    └── Reservation
```

### Key Considerations

- Each restaurant has its own tables, hours, and manager
- Customers can book at any location
- Analytics can be viewed per-restaurant or chain-wide

---

## 1.3 Implement Status Workflows

### Reservation Status Flow

```
    ┌──────────────┐
    │   PENDING    │ ← Just created
    └──────┬───────┘
           │ confirm()
           ▼
    ┌──────────────┐
    │  CONFIRMED   │ ← Approved by restaurant
    └──────┬───────┘
           │ checkIn()
           ▼
    ┌──────────────┐
    │  COMPLETED   │ ← Customer showed up
    └──────────────┘

    At any point before COMPLETED:
    cancel() → CANCELLED
    noShow() → NO_SHOW (from CONFIRMED only)
```

### Payment Status Flow (for Phase 5)

```
    PENDING → PAID → REFUNDED
                ↓
           PARTIALLY_REFUNDED
```

---

## 1.4 Unit Testing

### Minimum Test Coverage

Write tests for your service layer:

- Test successful reservation creation
- Test double-booking prevention
- Test capacity validation
- Test opening hours validation
- Test status transitions
- Test cancellation rules

### Test Structure

```
src/test/java/com/dumplinghouse/
├── service/
│   ├── ReservationServiceTest.java
│   ├── RestaurantServiceTest.java
│   └── CustomerServiceTest.java
└── controller/
    └── ReservationControllerIntegrationTest.java
```

---

## ✅ Phase 1 Checklist

- [ ] Code follows clean architecture principles
- [ ] All inputs are validated with clear messages
- [ ] Custom exceptions with proper HTTP status codes
- [ ] Multi-restaurant support implemented
- [ ] Reservation status workflow complete
- [ ] Unit tests cover critical business logic
- [ ] All tests pass

---

# Phase 2: Production Database 🗄️

**Goal:** Replace H2 with PostgreSQL and implement proper migrations

**Difficulty:** ⭐ (Beginner-friendly)

---

## 2.1 PostgreSQL Setup

### Why PostgreSQL?

- Production-grade reliability
- Required skill for professional developers
- Better performance and features than H2
- Industry standard

### Configuration

Update your application configuration for PostgreSQL connection with:

- Database URL
- Username and password
- Connection pool settings (HikariCP)

---

## 2.2 Database Migrations with Flyway

### What is Flyway?

Flyway is a version control system for your database schema. It tracks which changes have been applied and ensures every environment has the same database structure.

**Important:** Flyway handles database STRUCTURE (CREATE TABLE, ALTER TABLE). It is NOT an ORM. Hibernate/JPA handles data OPERATIONS (INSERT, SELECT, UPDATE).

### Migration File Structure

```
src/main/resources/db/migration/
├── V1__create_restaurants_table.sql
├── V2__create_tables_table.sql
├── V3__create_customers_table.sql
├── V4__create_reservations_table.sql
├── V5__add_phone_to_restaurants.sql
└── V6__create_users_and_roles.sql
```

### Naming Convention

```
V{version}__{description}.sql

Examples:
V1__create_restaurants_table.sql
V2__add_indexes.sql
V10__add_payment_fields.sql
```

### How It Works

1. Application starts
2. Flyway checks `flyway_schema_history` table
3. Compares with migration files
4. Applies new migrations in order
5. Records what was applied
6. Application continues with up-to-date schema

---

## 2.3 Update Hibernate Configuration

```
# Let Flyway handle schema, Hibernate only validates
spring.jpa.hibernate.ddl-auto=validate
```

This ensures Hibernate never modifies your schema — Flyway controls all structural changes.

---

## 2.4 Add Database Indexes

Create migrations to add indexes for frequently queried columns:

- Restaurant city
- Reservation date and status
- Customer identifiers
- Table restaurant associations

---

## ✅ Phase 2 Checklist

- [ ] PostgreSQL configured and running
- [ ] All tables created via Flyway migrations
- [ ] Hibernate set to validate mode only
- [ ] Indexes added for performance
- [ ] Application starts and all tests pass
- [ ] Can run fresh database from migrations alone

### 📚 Learning Resources

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [PostgreSQL Tutorial](https://www.postgresqltutorial.com/)
- [Spring Boot + Flyway Guide](https://www.baeldung.com/database-migrations-with-flyway)

---

# Phase 3: Security & Authentication 🔐

**Goal:** Implement enterprise-grade authentication with role-based access

**Difficulty:** ⭐⭐ (Intermediate)

---

## 3.1 JWT Authentication

### Authentication Flow

```
┌──────────────────────────────────────────────────────────────┐
│                    AUTHENTICATION FLOW                        │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│   1. User sends credentials (POST /api/auth/login)           │
│                         │                                     │
│                         ▼                                     │
│   2. Server validates credentials                             │
│                         │                                     │
│                         ▼                                     │
│   3. Server generates JWT containing:                         │
│      • User ID                                                │
│      • Roles (ADMIN, MANAGER, STAFF, CUSTOMER)               │
│      • Expiration time                                        │
│                         │                                     │
│                         ▼                                     │
│   4. Client stores JWT and sends with every request          │
│      Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...   │
│                         │                                     │
│                         ▼                                     │
│   5. Server validates JWT on each request                     │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

### Required Endpoints

```
POST /api/auth/register     ← Create new customer account
POST /api/auth/login        ← Get JWT token
POST /api/auth/refresh      ← Refresh expired token
GET  /api/auth/me           ← Get current user info
```

---

## 3.2 Role-Based Access Control (RBAC)

### Permission Matrix

| Endpoint                  | SUPER_ADMIN | MANAGER  | STAFF | CUSTOMER |
| ------------------------- | ----------- | -------- | ----- | -------- |
| Create restaurant         | ✅          | ❌       | ❌    | ❌       |
| Manage own restaurant     | ✅          | ✅ (own) | ❌    | ❌       |
| View all analytics        | ✅          | ❌       | ❌    | ❌       |
| View restaurant analytics | ✅          | ✅ (own) | ❌    | ❌       |
| Confirm reservations      | ✅          | ✅       | ✅    | ❌       |
| Make reservation          | ✅          | ✅       | ✅    | ✅       |
| View own reservations     | ✅          | ✅       | ✅    | ✅       |

### Manager Restaurant Binding

Managers can only access their assigned restaurant:

- Manager of Xi'an cannot see Beijing data
- Super Admin can see and manage everything

---

## 3.3 Secure Password Storage

- Use BCrypt for password hashing
- Never store plain text passwords
- Implement password strength requirements

---

## 3.4 Security Headers & Best Practices

- CORS configuration for your frontend domain
- Rate limiting to prevent abuse
- Input sanitization
- SQL injection prevention (use parameterized queries)

---

## ✅ Phase 3 Checklist

- [ ] JWT authentication implemented
- [ ] User registration and login work
- [ ] Role-based access control enforced
- [ ] Passwords hashed with BCrypt
- [ ] Protected endpoints require valid JWT
- [ ] Managers can only access their restaurant
- [ ] CORS configured for frontend
- [ ] Security tests pass

### 📚 Learning Resources

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT.io — Understanding JWTs](https://jwt.io/introduction)
- [Baeldung Spring Security JWT](https://www.baeldung.com/spring-security-oauth-jwt)

---

# Phase 4: Frontend Application 🎨

**Goal:** Build a modern, responsive frontend for customer reservations

**Difficulty:** ⭐⭐ (Intermediate)

---

## 4.1 Technology Stack

Choose **Vue.js** or **React** based on your preference or career goals.

### Option A: Vue.js Stack

| Purpose          | Library                  | Why                                  |
| ---------------- | ------------------------ | ------------------------------------ |
| Framework        | Vue 3                    | Modern, performant, popular in China |
| Build Tool       | Vite                     | Fast development                     |
| State Management | Pinia                    | Official, intuitive                  |
| Routing          | Vue Router               | SPA navigation                       |
| UI Components    | Element Plus or Naive UI | Popular in China                     |
| HTTP Client      | Axios                    | Standard for API calls               |
| Icons            | Iconify                  | Large icon collection                |

### Option B: React Stack

| Purpose          | Library                  | Why                               |
| ---------------- | ------------------------ | --------------------------------- |
| Framework        | React 19                 | Industry standard, huge ecosystem |
| Build Tool       | Vite                     | Fast development                  |
| State Management | Zustand or Redux Toolkit | Simple or powerful                |
| Routing          | React Router             | SPA navigation                    |
| UI Components    | shadcn/ui or Ant Design  | Modern and customizable           |
| HTTP Client      | Axios                    | Standard for API calls            |
| Icons            | Lucide React             | Clean, consistent icons           |

**Both are excellent choices.** Vue is slightly easier to learn, React has more job opportunities internationally.

### 📚 Recommended UI Libraries

**For React:**

- [shadcn/ui](https://ui.shadcn.com/) — Beautiful, accessible components built with Radix UI and Tailwind CSS. Copy-paste components you own and customize.
- [Ant Design](https://ant.design/) — Enterprise-level UI library by Alibaba, very popular in China.

**For Vue:**

- [Naive UI](https://www.naiveui.com/) — Modern Vue 3 component library, TypeScript-first, highly customizable.
- [Element Plus](https://element-plus.org/) — By Ele.me (Alibaba), widely used in Chinese companies.

### 🎨 Learn About Atomic Design

Before building your frontend, learn about **Atomic Design** methodology:

```
Atoms → Molecules → Organisms → Templates → Pages
```

**What is Atomic Design?**

- A methodology for creating design systems
- Components are organized from smallest (atoms) to largest (pages)
- Makes your code more reusable and maintainable
- Professional teams use this approach

**Examples:**

- **Atom:** Button, Input, Label
- **Molecule:** Search bar (input + button), Form field (label + input)
- **Organism:** Reservation form, Navigation header
- **Template:** Page layout with placeholders
- **Page:** Complete reservation page

**Resources:**

- [Atomic Design Pattern: How to structure your React application](https://medium.com/@janelle.wg/atomic-design-pattern-how-to-structure-your-react-application-2bb4d9ca5f97)
- Search "Atomic Design Vue" for Vue-specific tutorials

---

## 4.2 Application Structure

### Vue.js Structure

```
frontend/
├── src/
│   ├── api/              ← API service modules
│   ├── assets/           ← Images, fonts
│   ├── components/       ← Reusable components
│   ├── composables/      ← Shared logic (Vue 3)
│   ├── layouts/          ← Page layouts
│   ├── router/           ← Route definitions
│   ├── stores/           ← Pinia stores
│   ├── views/            ← Page components
│   ├── App.vue
│   └── main.js
├── index.html
├── vite.config.js
└── package.json
```

### React Structure

```
frontend/
├── src/
│   ├── api/              ← API service modules
│   ├── assets/           ← Images, fonts
│   ├── components/       ← Reusable components
│   ├── hooks/            ← Custom hooks
│   ├── layouts/          ← Page layouts
│   ├── pages/            ← Page components
│   ├── store/            ← State management
│   ├── App.jsx
│   └── main.jsx
├── index.html
├── vite.config.js
└── package.json
```

---

## 4.3 Customer Interface (This Phase)

**Focus on the customer reservation experience only.** Admin and manager interfaces will be built later (see Future Improvements).

### Pages to Build

**Home Page**

- City selector (Xi'an, Beijing, Chengdu)
- Restaurant information display
- Call-to-action to make reservation

**Reservation Flow**

1. Select city and restaurant
2. Choose date and time slot
3. Select number of guests
4. View available tables
5. Enter contact information
6. Confirm reservation
7. Receive confirmation screen

**My Reservations**

- List of upcoming reservations
- Past reservations history
- Cancel functionality
- Reservation details view

**User Authentication**

- Login page
- Registration page
- Simple profile page

---

## 4.4 Key Components to Build

### Reservation Form

```
┌────────────────────────────────────────────────┐
│         MAKE A RESERVATION                      │
│            预订餐位                              │
├────────────────────────────────────────────────┤
│                                                 │
│   📍 Select City                               │
│   ┌─────────────────────────────────────────┐  │
│   │  Xi'an  │  Beijing  │  Chengdu          │  │
│   └─────────────────────────────────────────┘  │
│                                                 │
│   📅 Select Date                               │
│   ┌─────────────────────────────────────────┐  │
│   │  [Calendar picker]                      │  │
│   └─────────────────────────────────────────┘  │
│                                                 │
│   🕐 Select Time                               │
│   ┌─────────────────────────────────────────┐  │
│   │  11:00 │ 12:00 │ 13:00 │ 18:00 │ 19:00 │  │
│   └─────────────────────────────────────────┘  │
│                                                 │
│   👥 Number of Guests                          │
│   ┌─────────────────────────────────────────┐  │
│   │  [ - ]     4 guests      [ + ]          │  │
│   └─────────────────────────────────────────┘  │
│                                                 │
│   ┌─────────────────────────────────────────┐  │
│   │        CHECK AVAILABILITY               │  │
│   └─────────────────────────────────────────┘  │
│                                                 │
└────────────────────────────────────────────────┘
```

### Reservation Confirmation

```
┌────────────────────────────────────────────────┐
│              ✓ BOOKING CONFIRMED               │
│                 预订成功                        │
├────────────────────────────────────────────────┤
│                                                 │
│   Reservation #: DH-2025-001234                │
│                                                 │
│   🏮 Dumpling House Xi'an                      │
│   📍 123 Tang Street, Xi'an                    │
│   📅 December 15, 2025                         │
│   🕐 19:00                                      │
│   👥 4 guests                                   │
│                                                 │
│   ─────────────────────────────────────────    │
│                                                 │
│   Please arrive 10 minutes before your         │
│   reservation time.                            │
│                                                 │
│   ┌─────────────────────────────────────────┐  │
│   │          VIEW MY RESERVATIONS           │  │
│   └─────────────────────────────────────────┘  │
│                                                 │
└────────────────────────────────────────────────┘
```

---

## 4.5 Responsive Design

### Breakpoints

- Mobile: < 768px (Primary focus — most users!)
- Tablet: 768px - 1024px
- Desktop: > 1024px

**Important:** Design mobile-first! Most Chinese users will access via smartphone.

---

## 4.6 Future Improvements (Not in This Phase)

The following will be addressed in later phases or as bonus features:

- 🔜 **Manager Dashboard** — Manage reservations, tables, view analytics
- 🔜 **Super Admin Interface** — Manage all restaurants, global analytics
- 🔜 **Payment Integration** — WeChat Pay deposit system

For now, focus on delivering an excellent customer reservation experience.

---

## ✅ Phase 4 Checklist

- [ ] Project setup with Vite (Vue or React)
- [ ] Home page with city/restaurant selection
- [ ] Reservation flow complete (all steps)
- [ ] My Reservations page with list and details
- [ ] Cancel reservation functionality
- [ ] User authentication (login/register)
- [ ] Responsive design (mobile-first)
- [ ] API integration with backend
- [ ] Loading states and error handling
- [ ] Form validation with clear messages

### 📚 Learning Resources

**Vue.js:**

- [Vue 3 Official Documentation](https://vuejs.org/guide/introduction.html)
- [Pinia State Management](https://pinia.vuejs.org/)
- [Element Plus Components](https://element-plus.org/)

**React:**

- [React Official Documentation](https://react.dev/learn)
- [Zustand State Management](https://zustand-demo.pmnd.rs/)
- [Ant Design Components](https://ant.design/)

---

# Phase 5: Analytics with Metabase 📊

**Goal:** Install Metabase with Docker and create dashboards for Super Admin and Manager roles

**Difficulty:** ⭐ (Beginner-friendly)

---

## 5.1 What is Metabase?

**Website:** [https://www.metabase.com](https://www.metabase.com)

Metabase is an open-source business intelligence tool that connects to your database and allows creating dashboards **without writing code**.

**Why Metabase instead of building dashboards?**

- No frontend development needed
- Professional visualizations out of the box
- Easy setup with Docker
- Role-based access built-in
- Free and open-source
- Used by real companies

**This is how real companies handle analytics** — they don't build dashboards from scratch.

---

## 5.2 Metabase Installation with Docker

### Add to docker-compose.yml

Add Metabase as a service in your Docker Compose configuration:

```yaml
metabase:
  image: metabase/metabase:latest
  container_name: dumplinghouse-metabase
  ports:
    - "3001:3000"
  environment:
    - MB_DB_TYPE=postgres
    - MB_DB_DBNAME=metabase
    - MB_DB_PORT=5432
    - MB_DB_USER=metabase
    - MB_DB_PASS=metabase_password
    - MB_DB_HOST=db
  depends_on:
    - db
  restart: unless-stopped
```

### Access Metabase

After running `docker-compose up -d`:

- Open http://localhost:3001
- Complete the setup wizard
- Connect to your PostgreSQL database

---

## 5.3 Database Connection

### Connect Metabase to Your Application Database

During Metabase setup, configure the connection:

| Setting       | Value                    |
| ------------- | ------------------------ |
| Database type | PostgreSQL               |
| Host          | db (Docker network name) |
| Port          | 5432                     |
| Database name | dumplinghouse            |
| Username      | your_db_user             |
| Password      | your_db_password         |

Metabase will automatically discover your tables and relationships.

---

## 5.4 User Roles in Metabase

### Create Two User Groups

**1. Super Admin Group (总管理员)**

- Can see ALL restaurants' data
- Access to chain-wide analytics
- Can create and modify dashboards

**2. Manager Group (店长)**

- Can see ONLY their restaurant's data
- Limited to pre-built dashboards
- Cannot modify dashboard structure

### Setting Up Data Permissions

In Metabase Admin → Permissions:

| Group       | Restaurants Table          | Reservations               | Tables                     |
| ----------- | -------------------------- | -------------------------- | -------------------------- |
| Super Admin | Full access                | Full access                | Full access                |
| Manager     | Row-level (own restaurant) | Row-level (own restaurant) | Row-level (own restaurant) |

**Row-level security** ensures managers only see their own restaurant's data.

---

## 5.5 Super Admin Dashboard

### Dashboard: Chain Overview (连锁店总览)

Create a dashboard with these visualizations:

**Key Metrics (Top Row)**

```
┌─────────────────┬─────────────────┬─────────────────┬─────────────────┐
│ Total           │ Today's         │ Cancellation    │ Average         │
│ Reservations    │ Reservations    │ Rate            │ Party Size      │
│    1,234        │      45         │     8.5%        │     3.2         │
│   This Month    │   All Stores    │   This Month    │   This Month    │
└─────────────────┴─────────────────┴─────────────────┴─────────────────┘
```

**Reservations by Restaurant (Bar Chart)**

```
Reservations This Month by Location

Xi'an     ████████████████████  456
Beijing   ██████████████████████████  523
Chengdu   ███████████████  312
```

**Cancellation Rate by Restaurant (Table)**

| Restaurant | Total Reservations | Cancelled | Cancellation Rate |
| ---------- | ------------------ | --------- | ----------------- |
| Xi'an      | 456                | 32        | 7.0%              |
| Beijing    | 523                | 58        | 11.1%             |
| Chengdu    | 312                | 21        | 6.7%              |

**Reservation Trends (Line Chart)**

- X-axis: Date (last 30 days)
- Y-axis: Number of reservations
- One line per restaurant

**Reservations by Status (Pie Chart)**

- PENDING
- CONFIRMED
- COMPLETED
- CANCELLED
- NO_SHOW

---

## 5.6 Manager Dashboard

### Dashboard: My Restaurant (我的餐厅)

Each manager sees only their restaurant's data.

**Key Metrics (Top Row)**

```
┌─────────────────┬─────────────────┬─────────────────┬─────────────────┐
│ Today's         │ This Week       │ Pending         │ Cancellation    │
│ Reservations    │ Reservations    │ Confirmations   │ Rate            │
│      12         │      67         │       3         │     5.2%        │
└─────────────────┴─────────────────┴─────────────────┴─────────────────┘
```

**Today's Reservations (Table)**

| Time  | Guests | Customer | Phone           | Status    |
| ----- | ------ | -------- | --------------- | --------- |
| 11:00 | 2      | 张三     | 138\*\*\*\*1234 | CONFIRMED |
| 12:00 | 4      | 李四     | 139\*\*\*\*5678 | PENDING   |
| 18:00 | 6      | 王五     | 137\*\*\*\*9012 | CONFIRMED |

**Reservations by Time Slot (Bar Chart)**

- Shows which time slots are most popular
- Helps with staffing decisions

**Weekly Overview (Calendar Heatmap)**

- Visual representation of busy vs slow days

**Recent Cancellations (Table)**

| Date  | Customer | Guests | Reason           |
| ----- | -------- | ------ | ---------------- |
| Dec 5 | 赵六     | 4      | Customer request |
| Dec 3 | 钱七     | 2      | No show          |

---

## 5.7 Creating Questions (Queries) in Metabase

### Example: Reservations by Restaurant

**Simple Mode (No SQL needed):**

1. Click "New" → "Question"
2. Select "reservations" table
3. Add columns: restaurant_id, count
4. Group by: restaurant_id
5. Visualize as bar chart

### Example: Cancellation Rate by Restaurant

**SQL Mode (For complex queries):**

```sql
SELECT
    r.name as restaurant_name,
    COUNT(*) as total_reservations,
    SUM(CASE WHEN res.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled,
    ROUND(
        SUM(CASE WHEN res.status = 'CANCELLED' THEN 1 ELSE 0 END) * 100.0 / COUNT(*),
        1
    ) as cancellation_rate
FROM reservations res
JOIN restaurants r ON res.restaurant_id = r.id
WHERE res.created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY r.id, r.name
ORDER BY cancellation_rate DESC
```

---

## 5.8 Dashboard Access URLs

After setup, your dashboards will be accessible at:

```
Super Admin Dashboard:
http://localhost:3001/dashboard/1

Manager Dashboard (with restaurant filter):
http://localhost:3001/dashboard/2?restaurant_id=1
```

**Pro tip:** You can embed these dashboards in your Vue/React app using iframes (optional).

---

## ✅ Phase 5 Checklist

- [ ] Metabase added to docker-compose.yml
- [ ] Metabase running and connected to PostgreSQL
- [ ] Super Admin user group created
- [ ] Manager user group created with row-level security
- [ ] Super Admin dashboard with chain-wide metrics
- [ ] Manager dashboard with single-restaurant metrics
- [ ] Reservation tracking visualizations
- [ ] Cancellation rate tracking
- [ ] Both dashboards tested with sample data

### 📚 Learning Resources

- [Metabase Documentation](https://www.metabase.com/docs/latest/)
- [Metabase Permissions Guide](https://www.metabase.com/docs/latest/permissions/introduction)
- [Creating Questions in Metabase](https://www.metabase.com/docs/latest/questions/start)

---

# Phase 6: DevOps & Deployment 🐳

**Goal:** Containerize everything and create professional deployment pipeline

**Difficulty:** ⭐⭐ (Intermediate)

---

## 6.1 Docker Architecture

### Container Structure

```
┌─────────────────────────────────────────────────────────────┐
│                    DOCKER COMPOSE                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐       │
│   │   nginx     │   │    api      │   │  frontend   │       │
│   │   :80       │──▶│   :8080     │   │   :3000     │       │
│   │ (proxy)     │   │ (Spring)    │   │ (Vue/React) │       │
│   └─────────────┘   └──────┬──────┘   └─────────────┘       │
│                            │                                 │
│                            ▼                                 │
│          ┌─────────────────────────────────┐                │
│          │                                 │                │
│   ┌──────┴──────┐                  ┌───────┴─────┐          │
│   │     db      │                  │    redis    │          │
│   │   :5432     │                  │   :6379     │          │
│   │ (postgres)  │                  │  (cache)    │          │
│   └─────────────┘                  └─────────────┘          │
│                                                              │
│   ┌─────────────┐                                           │
│   │  metabase   │                                           │
│   │   :3001     │                                           │
│   │ (analytics) │                                           │
│   └─────────────┘                                           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Services Explained

| Service  | Purpose                        | Port    |
| -------- | ------------------------------ | ------- |
| nginx    | Reverse proxy, SSL termination | 80, 443 |
| api      | Spring Boot backend            | 8080    |
| frontend | Vue.js or React application    | 3000    |
| db       | PostgreSQL database            | 5432    |
| redis    | Caching layer                  | 6379    |
| metabase | Analytics dashboards           | 3001    |

---

## 6.2 Environment Configuration

### Environment Separation

Create separate configurations for:

- **Development:** Local development with hot reload
- **Testing:** Automated test execution
- **Production:** Optimized, secure deployment

### Secrets Management

Never commit secrets to Git:

- Database passwords
- JWT secret keys
- API keys

Use environment variables or Docker secrets.

---

## 6.3 One-Command Startup

The goal is that anyone can run the entire system with:

```bash
git clone https://github.com/yourname/dumpling-house.git
cd dumpling-house
docker-compose up -d
```

Then access:

- Frontend: http://localhost:3000
- API Docs: http://localhost:8080/swagger-ui.html
- Analytics: http://localhost:3001

---

## 6.4 Optional: CI/CD Pipeline

### GitHub Actions Workflow

Create automated pipeline that:

1. Runs on every push
2. Executes all tests
3. Builds Docker images
4. Pushes to container registry
5. Deploys to server (if on main branch)

### Code Quality Gates

- All tests must pass
- Code coverage minimum (e.g., 70%)
- No critical security vulnerabilities
- Linting rules pass

---

## ✅ Phase 6 Checklist

- [ ] Dockerfile for Spring Boot application
- [ ] Dockerfile for Vue.js or React application
- [ ] docker-compose.yml with all services (including Metabase)
- [ ] Environment variables properly configured
- [ ] One command starts entire system
- [ ] Volumes configured for data persistence
- [ ] Health checks for all services
- [ ] Optional: CI/CD pipeline configured

### 📚 Learning Resources

- [Docker Getting Started](https://docs.docker.com/get-started/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

# Phase 7: Cloud Deployment ☁️

**Goal:** Deploy your application online with a live demo URL

**Difficulty:** ⭐⭐ (Intermediate)

---

## 7.1 Good News: Free Alibaba Cloud for Students! 🎓

As students at a Chinese university, you are eligible for **free or very cheap** Alibaba Cloud hosting!

### Alibaba Cloud Student Program (阿里云学生计划)

**Website:** [https://edu.aliyun.com](https://edu.aliyun.com)

### What You Get

| Service               | Free Tier for Students                     |
| --------------------- | ------------------------------------------ |
| **ECS (云服务器)**    | Free 1 month, then ~¥1/month for 12 months |
| **Configuration**     | 1 vCPU, 1GB RAM, 20-40GB SSD               |
| **ApsaraDB MySQL**    | Free limited tier (512MB)                  |
| **Domain name**       | Free .xyz or .top domain for 1 year        |
| **Technical support** | Basic support included                     |

**This is enough to deploy your complete fullstack application!**

### How to Register

1. **Create Alibaba Cloud account**

   - Go to [https://edu.aliyun.com](https://edu.aliyun.com)
   - Register with your **Chinese phone number**

2. **Navigate to Student Zone**

   - Find "学生专区" (Student Zone)
   - Click on student offers

3. **Verify your student status**

   - Upload your **student card** (学生证)
   - Or link your **university account**
   - **Chinese ID card** (身份证) is required for verification

4. **Claim your free resources**
   - Select ECS instance
   - Choose your configuration
   - Deploy!

### Duration

- Valid for **12 months**
- **Renewable** as long as you're enrolled as a student
- Your university may have additional partnerships with Alibaba Cloud

---

## 7.2 Recommended Architecture

### Complete Stack on Single ECS Instance

```
┌─────────────────────────────────────────────────────────────┐
│                ALIBABA CLOUD ECS (学生版)                    │
│                   1 vCPU / 1GB RAM                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   ┌─────────────────────────────────────────────────────┐   │
│   │                    NGINX                             │   │
│   │              (Reverse Proxy)                         │   │
│   │                   :80/:443                           │   │
│   └─────────────────────┬───────────────────────────────┘   │
│                         │                                    │
│          ┌──────────────┼──────────────┐                    │
│          ▼              ▼              ▼                    │
│   ┌────────────┐ ┌────────────┐ ┌────────────┐             │
│   │  Frontend  │ │  Backend   │ │  Metabase  │             │
│   │ Vue/React  │ │  Spring    │ │ Analytics  │             │
│   │  (static)  │ │   :8080    │ │   :3001    │             │
│   └────────────┘ └─────┬──────┘ └─────┬──────┘             │
│                        │              │                     │
│                        ▼              ▼                     │
│                 ┌─────────────────────────┐                 │
│                 │      PostgreSQL         │                 │
│                 │         :5432           │                 │
│                 └─────────────────────────┘                 │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Alternative: Split Frontend and Backend

```
┌─────────────────────────────────────────────────────────────┐
│                    DEPLOYMENT OPTION B                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   FRONTEND (Static)           BACKEND + DATABASE            │
│   ┌─────────────────┐        ┌─────────────────┐           │
│   │  Gitee Pages    │───────▶│  Alibaba ECS    │           │
│   │    (Free)       │  API   │  (Student plan) │           │
│   │                 │ calls  │                 │           │
│   │ yourname.gitee  │        │ Spring Boot +   │           │
│   │ .io/restaurant  │        │ PostgreSQL +    │           │
│   │                 │        │ Metabase        │           │
│   └─────────────────┘        └─────────────────┘           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 7.3 Cost Summary

| Setup                     | Monthly Cost                       |
| ------------------------- | ---------------------------------- |
| **Alibaba ECS (student)** | ~¥1/month (after first free month) |
| **Gitee Pages**           | Free                               |
| **Domain (.xyz)**         | Free for 1 year                    |
| **SSL Certificate**       | Free (Let's Encrypt)               |
| **Total**                 | **~¥1/month (~$0.14 USD)**         |

**Essentially free for students!**

---

## 7.4 Security Best Practices

⚠️ **Important security rules:**

| Rule                                  | Why                                   |
| ------------------------------------- | ------------------------------------- |
| **Never expose database port (5432)** | Hackers scan for open DB ports        |
| **Use .env files for secrets**        | Never commit passwords to Git         |
| **Configure Security Groups**         | Only open ports 80, 443, and 22       |
| **Whitelist SSH access**              | Only allow your IP to connect via SSH |
| **Use strong passwords**              | For database and server access        |

### Recommended Security Group Configuration

| Port | Protocol | Source       | Purpose    |
| ---- | -------- | ------------ | ---------- |
| 80   | TCP      | 0.0.0.0/0    | HTTP       |
| 443  | TCP      | 0.0.0.0/0    | HTTPS      |
| 22   | TCP      | Your IP only | SSH access |

**Block everything else!**

---

## 7.5 Deployment Steps

### Step 1: Register for Student Program

1. Go to [edu.aliyun.com](https://edu.aliyun.com)
2. Create account with Chinese phone number
3. Verify student status with student card + ID
4. Claim free ECS instance

### Step 2: Configure ECS Instance

1. Choose **Ubuntu 22.04** as operating system
2. Set a **strong root password**
3. Configure **Security Group** (ports 80, 443, 22)
4. Note your **public IP address**

### Step 3: Install Docker

SSH into your server and run:

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com | sh

# Install Docker Compose
sudo apt install docker-compose -y

# Add your user to docker group
sudo usermod -aG docker $USER
```

### Step 4: Deploy Your Application

1. Clone your repository
2. Configure environment variables
3. Run `docker-compose up -d`
4. Configure Nginx as reverse proxy

### Step 5: Configure Domain (Optional)

1. Claim free domain from student program
2. Point DNS to your ECS IP
3. Install SSL certificate with Let's Encrypt

---

## 7.6 Using Gitee for Version Control

Since GitHub can be slow in China, use **Gitee** for your repositories:

| Feature            | Gitee                       |
| ------------------ | --------------------------- |
| Speed in China     | ⚡ Excellent                |
| Free private repos | ✅ Yes                      |
| CI/CD              | ✅ Available                |
| Pages hosting      | ✅ Free for static sites    |
| Language           | Chinese interface available |

### Gitee Pages (for Frontend)

If you want to host frontend separately:

1. Build your Vue/React project: `npm run build`
2. Push `dist` folder to Gitee repository
3. Enable Gitee Pages in settings
4. Access at `yourname.gitee.io/project-name`

---

## 7.7 What Recruiters See

After deployment:

```
┌─────────────────────────────────────────────────────────────┐
│                    LIVE DEMO LINKS                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   🎯 "Click here to try my application"                      │
│                                                              │
│   ✅ Working application (not just code)                     │
│   ✅ Fast access from China                                  │
│   ✅ Real database with demo data                            │
│   ✅ Professional UI                                         │
│   ✅ API documentation                                       │
│                                                              │
│   This proves you can BUILD and SHIP software.              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**A live demo is worth 1000 lines of code on Gitee.**

---

## ✅ Phase 7 Checklist

- [ ] Register for Alibaba Cloud student program
- [ ] Verify student status (student card + ID)
- [ ] Claim free/discounted ECS instance
- [ ] Install Docker on ECS
- [ ] Configure Security Groups properly
- [ ] Deploy application with docker-compose
- [ ] Configure Nginx reverse proxy
- [ ] Test application accessibility
- [ ] Add demo data
- [ ] Optional: Configure custom domain with SSL
- [ ] Document live URLs in README

### 📚 Learning Resources

- [Alibaba Cloud Student Program](https://edu.aliyun.com)
- [Gitee Documentation](https://gitee.com/help)
- [Docker Installation Guide](https://docs.docker.com/engine/install/ubuntu/)
- [Nginx Configuration](https://nginx.org/en/docs/)

---

# Phase 8: Professional Documentation 📚

**Goal:** Create portfolio-ready documentation

**Difficulty:** ⭐ (Beginner-friendly)

---

## 7.1 README Structure

Your README is often the first thing recruiters see. Make it professional.

### Recommended Sections

**Header**

- Project name with logo/emoji
- One-line description
- Badges (build status, coverage, license)

**Live Demo Links**

- Customer application URL
- Manager dashboard URL
- API documentation URL
- Analytics dashboard URL

**Screenshots**

- 4-6 high-quality screenshots
- Show key features
- Include mobile views

**Features**

- Organized by user role
- Highlight technical achievements

**Tech Stack**

- Clear table of technologies used
- Explain why each was chosen

**Architecture**

- System architecture diagram
- Database schema diagram
- API flow diagrams

**Quick Start**

- Prerequisites
- Step-by-step setup
- Verification steps

**API Documentation**

- Link to Swagger/OpenAPI
- Key endpoints summary

**Project Structure**

- Directory tree
- Explain organization

**Testing**

- How to run tests
- Coverage information

**Contributing**

- Guidelines for contributions (even if solo project, shows professionalism)

**License**

- MIT or your choice

**Author**

- Your name
- Contact information
- LinkedIn profile

---

## 7.2 API Documentation

### Swagger/OpenAPI

Integrate Swagger UI for interactive API documentation:

- All endpoints documented
- Request/response examples
- Authentication explained
- Error responses defined

---

## 7.3 Architecture Diagrams

Create clear diagrams for:

**System Architecture**

- Show all components and their connections
- Include external services

**Database Schema**

- Entity relationship diagram
- Show foreign keys and cardinality

**Sequence Diagrams**

- Reservation flow
- Authentication flow
- Payment flow

Use tools like:

- draw.io (free)
- Mermaid (in markdown)
- Excalidraw

---

## 7.4 Demo Video

Create a 3-5 minute video showing:

- Customer making a reservation
- Manager confirming reservation
- Analytics dashboard
- Mobile responsiveness

Upload to Bilibili (哔哩哔哩) and embed link in README.

---

## ✅ Phase 8 Checklist

- [ ] Professional README with all sections
- [ ] Screenshots of key features
- [ ] Architecture diagrams created
- [ ] Swagger/OpenAPI documentation
- [ ] Database schema diagram
- [ ] Demo video recorded
- [ ] All links working
- [ ] No spelling/grammar errors

---

# Phase 9: Future Vision — WeChat Mini Program 🔮

**Goal:** Document how the system could be extended to WeChat Mini Program

**Difficulty:** ⭐⭐ (Intermediate — documentation only)

---

## 9.1 Why WeChat Matters in China

In China, WeChat is not just a messaging app — it's an entire ecosystem:

- 1.2+ billion users
- Primary payment method (WeChat Pay)
- Mini Programs replace many native apps
- Nobody checks email for service notifications

A restaurant reservation system in China would ideally have a WeChat Mini Program interface.

---

## 9.2 Good News: Your Backend Barely Changes

The architecture you built is already ready for WeChat integration:

```
┌─────────────────────────────────────────────────────────────┐
│                 CURRENT ARCHITECTURE                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   Vue.js / React ──────────▶ Spring Boot API ──▶ PostgreSQL │
│      (Frontend)                 (Backend)         (Database) │
│                                                              │
└─────────────────────────────────────────────────────────────┘

                         ⬇️ With WeChat ⬇️

┌─────────────────────────────────────────────────────────────┐
│                 WECHAT ARCHITECTURE                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   WeChat Mini Program ─────▶ Spring Boot API ──▶ PostgreSQL │
│      (New Frontend)          (Same Backend!)      (Same DB!) │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Your REST API serves both interfaces.** The backend is the single source of truth.

### What Stays the Same (90%+)

- All API endpoints
- Business logic
- Database schema
- Authentication flow (adapted for WeChat tokens)
- Validation rules

### What Changes

- Frontend code (WeChat uses its own framework)
- Authentication method (WeChat Login instead of email/password)
- Payment integration (WeChat Pay instead of simulation)

---

## 9.3 Mini Program Technology

Mini Programs use WeChat's proprietary framework:

| Web Technology | Mini Program Equivalent       |
| -------------- | ----------------------------- |
| HTML           | WXML (WeChat Markup Language) |
| CSS            | WXSS (WeChat Style Sheets)    |
| JavaScript     | JavaScript (with WeChat APIs) |
| Vue/React      | WeChat's component framework  |

The syntax is similar to Vue.js, so if you learned Vue, the transition is manageable.

### Development Tools

- **WeChat DevTools** (微信开发者工具)
- Available for Windows and Mac
- Includes emulator and debugger
- Test on your phone via QR scan

---

## 9.4 What to Write in Your README

Add a "Future Roadmap" or "WeChat Integration" section:

```markdown
## 🔮 Future: WeChat Mini Program

This application is designed with WeChat integration in mind:

- **Backend Ready:** REST API can serve both web and Mini Program clients
- **Authentication:** Endpoints support WeChat Login flow
- **Database:** User model includes fields for WeChat OpenID

To deploy as a Mini Program:

1. Register Mini Program with WeChat
2. Build frontend using WXML/WXSS/JS
3. Connect to existing API endpoints
4. Integrate WeChat Pay for deposits

The backend requires minimal changes — primarily adding WeChat
authentication token exchange.
```

This shows recruiters you understand the Chinese tech ecosystem without requiring actual WeChat integration.

---

## ✅ Phase 9 Checklist

- [ ] Understand why WeChat Mini Programs matter in China
- [ ] Document in README that backend is "WeChat-ready"
- [ ] Explain the architecture (same API, different frontend)
- [ ] Mention Mini Program technology stack
- [ ] Show understanding of Chinese market requirements

---

# Phase 10: Project Adaptability 🔄

**Goal:** Understand how this project can be rebuilt with different technologies

**Difficulty:** ⭐⭐ (Intermediate)

---

## 10.1 The Power of Architecture

The architecture you've built is **technology-agnostic**. The patterns and structure remain the same even if you swap out the technologies:

```
┌─────────────────────────────────────────────────────────────┐
│                 ARCHITECTURE = CONSTANT                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   [Frontend] ←──REST API──→ [Backend] ←──SQL──→ [Database]  │
│                                                              │
│   The PATTERN stays the same.                               │
│   The TECHNOLOGIES can change.                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**This is what senior developers understand:** frameworks come and go, but architecture principles remain.

---

## 10.2 Frontend Alternatives

You can rebuild the frontend with the other framework:

| If You Built With | Try Rebuilding With |
| ----------------- | ------------------- |
| Vue.js            | **React**           |
| React             | **Vue.js**          |

### What Changes

- Component syntax
- State management library (Pinia ↔ Zustand)
- Routing library (Vue Router ↔ React Router)
- Build configuration

### What Stays the Same

- API calls (same endpoints)
- UI/UX design
- Business logic in frontend
- Authentication flow

### Comparison: Vue vs React

| Concept      | Vue.js                  | React                  |
| ------------ | ----------------------- | ---------------------- |
| Template     | `<template>`            | JSX in return          |
| State        | `ref()` / `reactive()`  | `useState()`           |
| Computed     | `computed()`            | `useMemo()`            |
| Side effects | `watch()`               | `useEffect()`          |
| Store        | Pinia                   | Zustand / Redux        |
| Router       | Vue Router              | React Router           |
| UI Library   | Element Plus / Naive UI | shadcn/ui / Ant Design |

**Pro tip:** Rebuilding your frontend in the other framework is an excellent learning exercise and shows employers you're not tied to one technology.

---

## 10.3 Backend Alternatives

The backend can be completely rewritten in another language:

| Original         | Alternative           | Difficulty        |
| ---------------- | --------------------- | ----------------- |
| Java/Spring Boot | **C# / ASP.NET Core** | ⭐⭐ Medium       |
| Java/Spring Boot | **Node.js / Express** | ⭐⭐ Medium       |
| Java/Spring Boot | **Node.js / NestJS**  | ⭐⭐ Medium       |
| Java/Spring Boot | **Python / FastAPI**  | ⭐⭐ Medium       |
| Java/Spring Boot | **Python / Django**   | ⭐⭐ Medium       |
| Java/Spring Boot | **Go / Gin**          | ⭐⭐⭐ Higher     |
| Java/Spring Boot | **Rust / Actix**      | ⭐⭐⭐⭐ Advanced |

### What Changes

- Language syntax
- Framework conventions
- Package manager (Maven → npm, NuGet, pip, etc.)
- ORM library

### What Stays the Same

- REST API endpoints (same URLs, same JSON)
- Database schema (same tables)
- Business logic (same rules)
- Authentication concept (JWT)

### Technology Mapping

| Concept         | Java/Spring   | C#/ASP.NET           | Node.js        | Python         |
| --------------- | ------------- | -------------------- | -------------- | -------------- |
| Framework       | Spring Boot   | ASP.NET Core         | Express/NestJS | FastAPI/Django |
| ORM             | Hibernate/JPA | Entity Framework     | Prisma/TypeORM | SQLAlchemy     |
| Package Manager | Maven/Gradle  | NuGet                | npm/yarn       | pip            |
| JWT Library     | jjwt          | System.IdentityModel | jsonwebtoken   | PyJWT          |
| Migrations      | Flyway        | EF Migrations        | Prisma Migrate | Alembic        |

---

## 10.4 Database Alternatives

The database can also be swapped:

| Original   | Alternative    | Changes Required      |
| ---------- | -------------- | --------------------- |
| PostgreSQL | **MySQL**      | Minor SQL syntax      |
| PostgreSQL | **SQL Server** | Minor SQL syntax      |
| PostgreSQL | **SQLite**     | Simpler, good for dev |
| PostgreSQL | **MongoDB**    | Major (NoSQL)         |

### Relational → Relational (Easy)

Switching between PostgreSQL, MySQL, and SQL Server requires minimal changes:

- Update connection string
- Adjust minor SQL syntax differences
- Change database driver

### Relational → NoSQL (Major Rewrite)

Switching to MongoDB requires rethinking your data model:

- No foreign keys
- Embedded documents vs references
- Different query patterns

**Recommendation:** Stick with relational databases for this project.

---

## 10.5 Why This Matters for Your Career

### Interview Scenario

**Interviewer:** "I see you built this in Java/Spring Boot. We use Node.js here. Can you adapt?"

**Strong Answer:** "Absolutely. The architecture I've built follows REST principles and clean architecture patterns. The same project structure—controllers, services, repositories—exists in Express or NestJS. I've documented my API contracts, so rebuilding in Node.js would be straightforward. In fact, I've considered doing exactly that to demonstrate my versatility."

### Portfolio Strategy

Consider having **two versions** of your project:

```
┌─────────────────────────────────────────────────────────────┐
│                    PORTFOLIO SHOWCASE                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   Version 1: Java/Spring Boot + Vue.js                      │
│   → Shows your exam work evolved                            │
│                                                              │
│   Version 2: Node.js/NestJS + React                         │
│   → Shows you can work in different stacks                  │
│                                                              │
│   Same features, same API, different technologies.          │
│   This is VERY impressive to employers.                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 10.6 Step-by-Step: Rebuilding in Another Stack

If you want to rebuild in a different technology:

### Step 1: Document Your Current API

Before changing anything, document:

- All endpoints (URL, method, request body, response)
- Authentication flow
- Business rules
- Database schema

**Tools:** Use Swagger/OpenAPI export from your current project.

### Step 2: Set Up New Project

Create new project with chosen technology:

- Same folder structure concept (controllers, services, etc.)
- Same database schema
- Same environment variables

### Step 3: Implement Endpoint by Endpoint

Work through each endpoint:

1. Create the route
2. Implement the service logic
3. Test against same database
4. Compare response with original

### Step 4: Test with Same Frontend

Your frontend should work with both backends:

- Point to new backend URL
- All features should work identically
- This proves your API contract is solid

---

## 10.7 Suggested Learning Paths

### Path A: Frontend Developer Focus

```
Vue.js or React (done) → Learn the other one → Mobile (React Native or Flutter)
```

### Path B: Backend Developer Focus

```
Java/Spring (done) → Node.js/NestJS → Go or C# → Microservices
```

### Path C: Full-Stack Versatility

```
Java + Vue (done) → Node.js + React → Different database (MongoDB)
```

### Path D: DevOps Focus

```
Docker (done) → Kubernetes → CI/CD → Cloud Architecture
```

---

## ✅ Phase 10 Checklist

- [ ] Understand that architecture patterns transcend technologies
- [ ] Identify equivalent libraries in other stacks
- [ ] Document your API contract thoroughly
- [ ] Consider rebuilding frontend in alternative framework
- [ ] Consider rebuilding backend in alternative language
- [ ] Prepare to discuss adaptability in interviews

### 📚 Learning Resources

**React (if you learned Vue):**

- [React Official Tutorial](https://react.dev/learn)

**Vue.js (if you learned React):**

- [Vue.js Official Guide](https://vuejs.org/guide/introduction.html)

**Node.js/NestJS (if you learned Spring):**

- [NestJS Documentation](https://docs.nestjs.com/)
- [Express.js Guide](https://expressjs.com/en/guide/routing.html)

**C#/ASP.NET Core:**

- [ASP.NET Core Tutorial](https://docs.microsoft.com/en-us/aspnet/core/)

---

# 🎯 Final Project Assessment

## Completion Levels

### ⭐ Level 1: Solid Foundation

Phases 1-2 complete

- Professional backend code
- PostgreSQL with migrations
- Basic tests

**Demonstrates:** Backend development competency

### ⭐⭐ Level 2: Full-Stack Developer

Phases 1-4 complete

- Everything in Level 1
- JWT authentication with RBAC
- Complete Vue.js or React frontend

**Demonstrates:** Full-stack development ability

### ⭐⭐⭐ Level 3: Production-Ready Developer

Phases 1-6 complete

- Everything in Level 2
- Metabase analytics dashboards
- Docker containerization

**Demonstrates:** Production-ready development skills

### ⭐⭐⭐⭐ Level 4: Cloud-Native Developer

Phases 1-7 complete

- Everything in Level 3
- Live deployment with working URL
- Accessible demo for recruiters

**Demonstrates:** Real-world deployment skills

### ⭐⭐⭐⭐⭐ Level 5: Industry Ready

All phases complete

- Everything in Level 4
- Professional documentation
- WeChat integration vision

**Demonstrates:** Professional software engineer ready for the industry

---

## Skills Demonstrated

| Skill                 | Where Demonstrated          |
| --------------------- | --------------------------- |
| Java/Spring Boot      | Backend API                 |
| Database Design       | PostgreSQL + Flyway         |
| Security              | JWT + RBAC                  |
| Frontend Development  | Vue.js or React application |
| Business Intelligence | Metabase dashboards         |
| DevOps                | Docker + deployment         |
| Cloud Deployment      | Gitee Pages + Alibaba Cloud |
| Documentation         | README + API docs           |
| System Design         | Architecture decisions      |

---

## Interview Talking Points

When presenting this project, be prepared to discuss:

1. **Design Decisions**

   - Why you chose certain technologies
   - How you structured the database
   - Trade-offs you considered

2. **Challenges Overcome**

   - Complex business logic (availability checking)
   - Security implementation
   - Performance considerations

3. **Deployment Experience**

   - How you deployed to cloud platforms
   - Environment configuration
   - Docker containerization

4. **Future Improvements**
   - What you would add with more time
   - How it would scale
   - WeChat Mini Program potential

---

## ⚠️ Common Mistakes to Avoid

Learning from others' mistakes will save you time:

| Mistake                            | Why It's Bad                     | What To Do Instead                      |
| ---------------------------------- | -------------------------------- | --------------------------------------- |
| Skipping Phase 1 improvements      | Technical debt accumulates       | Fix code quality before adding features |
| No Git commits during development  | Can't track progress or rollback | Commit after every working feature      |
| Hardcoding configuration           | Breaks deployment                | Use environment variables               |
| Ignoring mobile responsiveness     | Most users are on phones         | Test on mobile from the start           |
| No error handling in frontend      | Bad user experience              | Show friendly error messages            |
| Exposing sensitive data in Git     | Security risk                    | Use .gitignore and env variables        |
| Building everything before testing | Hard to debug                    | Test each phase before moving on        |
| Copying code without understanding | Can't explain in interviews      | Understand every line you write         |

---

# 🏁 Conclusion

This project, when completed through all phases, demonstrates:

- **Technical Depth:** Full-stack development with modern technologies
- **Professional Practices:** Clean code, testing, documentation
- **DevOps Knowledge:** Containerization and deployment
- **Deployment Skills:** Live application accessible via URL
- **Business Understanding:** Real-world restaurant industry needs

**This is not a toy project — it's a professional portfolio piece with a live URL.**

When an employer can click a link and see your working application, you have already proven more than most candidates ever will.

**Make it yours:** Choose a restaurant theme you love, give it personality, and build something you're proud to show.

Good luck! 加油! 🚀

---

_Document created for Java Programming Course — Prof. Baptiste Dupuis_
