-- Truncate tables to avoid duplicate data issues
TRUNCATE TABLE admins CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE hotel_amenities CASCADE;
TRUNCATE TABLE hotel CASCADE;
TRUNCATE TABLE room CASCADE;
TRUNCATE TABLE booking CASCADE;
TRUNCATE TABLE payment CASCADE;
TRUNCATE TABLE comment CASCADE;

-- Insert data into admins table
INSERT INTO admins (id, username, email, password, created_at, updated_at, role, enabled)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'admin_user', 'admin@example.com', '$2a$10$EHhMSQokdR5XORwFDUqcwu6nmbpokQfOXBA8FapkNFdVMrvAs3qpe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN', true);

-- Insert data into users table
INSERT INTO users (id, username, email, password, created_at, updated_at, role, balance, enabled)
VALUES
    ('123e4567-e89b-12d3-a456-426614174001', 'user1', 'user1@example.com', '$2a$10$EHhMSQokdR5XORwFDUqcwu6nmbpokQfOXBA8FapkNFdVMrvAs3qpe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 400000, true),
    ('123e4567-e89b-12d3-a456-426614174002', 'user2', 'user2@example.com', '$2a$10$EHhMSQokdR5XORwFDUqcwu6nmbpokQfOXBA8FapkNFdVMrvAs3qpe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 450000, true),
    ('123e4567-e89b-12d3-a456-426614174003', 'user3', 'user3@example.com', '$2a$10$EHhMSQokdR5XORwFDUqcwu6nmbpokQfOXBA8FapkNFdVMrvAs3qpe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 450000, true),
    ('123e4567-e89b-12d3-a456-426614174004', 'user4', 'user4@example.com', '$2a$10$EHhMSQokdR5XORwFDUqcwu6nmbpokQfOXBA8FapkNFdVMrvAs3qpe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 450000, true),
    ('123e4567-e89b-12d3-a456-426614174005', 'user5', 'user5@example.com', '$2a$10$EHhMSQokdR5XORwFDUqcwu6nmbpokQfOXBA8FapkNFdVMrvAs3qpe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 470000, true);

-- Insert data into hotel table
INSERT INTO hotel (id, name, location, description)
VALUES
    (1, 'Grand Royale', 'ABERDEEN', 'Granite City in the northeast, oil industry hub.'),
    (2, 'Sunset Sands', 'BELFAST', 'Capital and largest city, known for its shipbuilding history'),
    (3, 'Mountain Retreat', 'BIRMINGHAM', 'Second-largest city, industrial and cultural center in the Midlands.'),
    (4, 'Grandiose House', 'MANCHESTER', 'Major industrial and cultural hub in the northwest');

-- Insert data into hotel_amenities table
INSERT INTO hotel_amenities (hotel_id, amenity)
VALUES
    (1, 'Free Wi-Fi'),
    (1, 'Swimming Pool'),
    (1, 'Gym'),
    (2, 'Beach Access'),
    (2, 'Spa'),
    (2, 'Restaurant'),
    (3, 'Ski Resort'),
    (3, 'Hot Tub'),
    (3, 'Fireplace');

-- Insert data into room table
INSERT INTO room (id, room_type, price, available, hotel_id)
VALUES
    (1, 'DELUXE', 50000, true, 1),
    (2, 'SINGLE', 35000, true, 1),
    (3, 'DOUBLE', 100000, true, 2),
    (4, 'SUITE', 150000, true, 2);

-- Insert data into booking table
INSERT INTO booking (id, user_id, room_id, hotel_id, start_date, end_date, status, is_paid, total_price)
VALUES
    (10, '123e4567-e89b-12d3-a456-426614174001', 1, 1, CURRENT_TIMESTAMP + INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '5 days', 'CONFIRMED', true, 200000),
    (11, '123e4567-e89b-12d3-a456-426614174002', 2, 1, CURRENT_TIMESTAMP + INTERVAL '2 days', CURRENT_TIMESTAMP + INTERVAL '6 days', 'CONFIRMED', true, 140000),
    (12, '123e4567-e89b-12d3-a456-426614174003', 3, 2, CURRENT_TIMESTAMP + INTERVAL '3 days', CURRENT_TIMESTAMP + INTERVAL '7 days', 'CONFIRMED', true, 400000);

-- Insert data into payment table
INSERT INTO payment (id, booking_id, amount, success, user_id)
VALUES
    (10, 10, 200000, true, '123e4567-e89b-12d3-a456-426614174001'),
    (11, 11, 140000, true, '123e4567-e89b-12d3-a456-426614174002');

-- Insert data into comment table
INSERT INTO comment (id, hotel_id, user_id, user_name, content, created_at)
VALUES
    (3, 1, '123e4567-e89b-12d3-a456-426614174001', 'user1', 'This is a very nice hotel', CURRENT_TIMESTAMP),
    (4, 2, '123e4567-e89b-12d3-a456-426614174002', 'user2', 'This is a very nice hotel, it has good environment', CURRENT_TIMESTAMP),
    (5, 3, '123e4567-e89b-12d3-a456-426614174003', 'user3', 'This is a very nice hotel, it has good environment', CURRENT_TIMESTAMP);