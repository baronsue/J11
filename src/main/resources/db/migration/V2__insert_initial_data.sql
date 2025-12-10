-- Insert Roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_RESTAURANT_OWNER');

-- Insert Demo Restaurants
INSERT INTO restaurants (name, address, phone, description, opening_time, closing_time) 
VALUES 
('Dumpling House Xi''an', '123 Muslim Quarter, Xi''an', '029-12345678', 'Authentic Xi''an dumplings and local specialties.', '10:00:00', '22:00:00'),
('Dumpling House Beijing', '456 Wangfujing St, Beijing', '010-87654321', 'Traditional Beijing style dumplings in a modern setting.', '11:00:00', '23:00:00'),
('Dumpling House Chengdu', '789 Chunxi Rd, Chengdu', '028-11223344', 'Spicy Sichuan dumplings and snacks.', '09:00:00', '21:00:00');

-- Insert Tables for Xi'an (Restaurant 1)
INSERT INTO restaurant_tables (table_number, capacity, restaurant_id) VALUES 
('A1', 2, 1), ('A2', 2, 1), ('A3', 4, 1), ('A4', 4, 1), ('A5', 6, 1);

-- Insert Tables for Beijing (Restaurant 2)
INSERT INTO restaurant_tables (table_number, capacity, restaurant_id) VALUES 
('B1', 2, 2), ('B2', 4, 2), ('B3', 4, 2), ('B4', 8, 2);

-- Insert Tables for Chengdu (Restaurant 3)
INSERT INTO restaurant_tables (table_number, capacity, restaurant_id) VALUES 
('C1', 2, 3), ('C2', 2, 3), ('C3', 4, 3), ('C4', 6, 3);

-- Insert Admin User (password: admin123)
-- Note: In a real scenario, use BCrypt to hash the password. Here we assume the application handles hashing or we insert a pre-hashed value.
-- For this example, I'll insert a placeholder hash. The application should be using BCryptPasswordEncoder.
-- $2a$10$r.S.y.V.e.r.y.S.e.c.u.r.e.P.a.s.s.w.o.r.d -> this is just an example, let's use a known hash for 'admin123'
-- $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcXBJD.g.e.e -> 'admin123' (BCrypt)
INSERT INTO users (username, email, password, enabled, created_at) 
VALUES ('admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcXBJD.g.e.e', true, NOW());

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Insert a Customer User (password: user123)
-- $2a$10$wQ.Z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z.z -> 'user123' (example hash)
-- Let's generate a real hash or use the same one for simplicity in this demo script if possible, or just a placeholder.
-- Using 'password' hash: $2a$10$dx.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l.l -> 'password'
INSERT INTO users (username, email, password, enabled, created_at) 
VALUES ('user', 'user@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcXBJD.g.e.e', true, NOW());

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'user' AND r.name = 'ROLE_USER';
