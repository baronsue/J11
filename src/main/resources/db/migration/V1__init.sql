-- Restaurants
CREATE TABLE restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL
);

-- Restaurant Tables
CREATE TABLE restaurant_tables (
    id BIGSERIAL PRIMARY KEY,
    table_number VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT fk_table_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
    CONSTRAINT uq_restaurant_table UNIQUE (restaurant_id, table_number)
);

-- Customers
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50) NOT NULL
);

-- Reservations
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    reservation_date DATE NOT NULL,
    reservation_time TIME NOT NULL,
    number_of_guests INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    table_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    CONSTRAINT fk_reservation_table FOREIGN KEY (table_id) REFERENCES restaurant_tables (id),
    CONSTRAINT fk_reservation_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE INDEX idx_reservations_table_date ON reservations (table_id, reservation_date);
CREATE INDEX idx_reservations_customer ON reservations (customer_id);

-- Reviews
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    rating INTEGER NOT NULL,
    comment VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    restaurant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    CONSTRAINT fk_review_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
    CONSTRAINT fk_review_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_review_reservation FOREIGN KEY (reservation_id) REFERENCES reservations (id),
    CONSTRAINT uq_review_reservation UNIQUE (reservation_id)
);

CREATE INDEX idx_reviews_restaurant ON reviews (restaurant_id);
CREATE INDEX idx_reviews_customer ON reviews (customer_id);

-- Roles and Users for RBAC
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    restaurant_id BIGINT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE INDEX idx_users_restaurant ON users (restaurant_id);
CREATE INDEX idx_user_roles_role ON user_roles (role_id);
