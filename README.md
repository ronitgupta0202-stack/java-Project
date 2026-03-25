# Cinema Seat Ticket Booking System 🎬

The Cinema Seat Ticket Booking System is a Java-based application designed to automate the process of booking movie tickets. It allows users to view available movies, select show timings, choose seats, and book tickets efficiently.

## Features
- 📅 **Date Selection**: Choose show dates for the next 3 days.
- 🎞 **Movie Selection**: View current movies and their timings.
- 🍿 **Snack Ordering**: Integrated snack ordering system.
- 💳 **Payment Simulation**: Process simulated payments for both tickets and snacks.
- 🗄 **Persistent Storage**: Uses MySQL to store seat bookings.

---

## Prerequisites
- **Java Deployment Kit (JDK)**: Version 8 or higher.
- **MySQL Database**: To store booking data.
- **MySQL Connector/J**: JDBC driver for MySQL.

## Setup Instructions

### 1. Database Configuration
Run the provided `setup.sql` script in your MySQL environment:
```sql
SOURCE setup.sql;
```
*Note: The current configuration in `SeatBooking.java` uses `root` as the username and `vaniiiiii02` as the password. You may need to update these in `src/SeatBooking.java` to match your local setup.*

### 2. Running the Application
Ensure the `mysql-connector-j.jar` is in your classpath and run the `Main` class:
```bash
javac -cp ".;lib/mysql-connector-j.jar" src/*.java
java -cp ".;lib/mysql-connector-j.jar" src.Main
```

---

## Contributing
Feel free to fork this project and submit pull requests for any improvements or bug fixes!
