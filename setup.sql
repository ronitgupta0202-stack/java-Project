-- Database Setup for Cinema Seat Ticket Booking System
CREATE DATABASE IF NOT EXISTS cinema;
USE cinema;

CREATE TABLE IF NOT EXISTS seat_bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    seat_id VARCHAR(50) NOT NULL,
    movie_name VARCHAR(255) NOT NULL,
    selected_date DATE NOT NULL
);
