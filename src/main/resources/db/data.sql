truncate table admins cascade;
truncate table users cascade;
truncate table hotel cascade;
truncate table room cascade;
truncate table booking cascade;
truncate table payment cascade;

INSERT INTO admins (id, username, email, password, created_at, updated_at, role, enabled)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'admin_user','admin@example.com', 'hashed_password_here', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,  'ADMIN',true);

INSERT INTO users (id, username, email, password, created_at, updated_at, role, balance, enabled)
VALUES
    ('123e4567-e89b-12d3-a456-426614174001', 'user1', 'user1@example.com', 'hashed_password_here', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 40000, true),
    ('123e4567-e89b-12d3-a456-426614174002', 'user2', 'user2@example.com', 'hashed_password_here', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 45000, true),
    ('123e4567-e89b-12d3-a456-426614174003', 'user3', 'user3@example.com', 'hashed_password_here', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 45000, true),
    ('123e4567-e89b-12d3-a456-426614174004', 'user4', 'user4@example.com', 'hashed_password_here', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 45000, true),
    ('123e4567-e89b-12d3-a456-426614174005', 'user5', 'user5@example.com', 'hashed_password_here', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER', 47000, true);

INSERT INTO hotel (id,name, state, location, amenities, description)
VALUES (1,'Grand Royale','OSUN','Osogbo',ARRAY['Free Wi-Fi', 'Swimming Pool', 'Gym'],'A luxurious hotel located in the heart of New York City.'),
       (2,'Sunset Sands','OGUN','Abeokuta',ARRAY['Beach Access', 'Spa', 'Restaurant'],'A beachfront hotel offering stunning ocean views and top-notch amenities.'),
       (3,'Mountain Retreat', 'LAgos','Ikeja', ARRAY['Ski Resort', 'Hot Tub', 'Fireplace'],'A cozy mountain lodge perfect for winter getaways.');

INSERT INTO room (id,room_type,price,available,hotel_id)
VALUES (1,'DELUXE',50_000,true,1),
       (2,'SINGLE',35_000,true,1),
       (3,'DOUBLE',100_000,true,2),
        (4,'SUITE',150_000,true,2);

INSERT INTO  booking (id,user_id,room_id,hotel_id,start_date,end_date,booking_status,is_paid,total_price)
VALUES (1,'123e4567-e89b-12d3-a456-426614174001',1,2,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,50_000),
        (2,'123e4567-e89b-12d3-a456-426614174002',2,1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,35_000),
         (3,'123e4567-e89b-12d3-a456-426614174003',3,2,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,100_000);

INSERT INTO payment (id,booking_id,amount,success,user_id)
VALUES (1,1,50_000,true,"123e4567-e89b-12d3-a456-426614174001"),
       (2,2,35_000,true,"123e4567-e89b-12d3-a456-426614174002");

