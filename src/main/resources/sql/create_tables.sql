-- @author Mattias Fridsén
-- @project LibraryDBMS
-- @date 2/28/2023
-- @contact matfir-1@student.ltu.se

-- Creates all tables in the database

-- BASE TABLES ---------------------------------------------------------------------------------------------------------

-- Author

-- Publisher


-- User
CREATE TABLE `users` (
    userID INT AUTO_INCREMENT UNIQUE NOT NULL,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    allowedRentals INT NOT NULL,
    currentRentals INT NOT NULL,
    lateFee DOUBLE NOT NULL,
    allowedToRent TINYINT(1) NOT NULL,
    deleted TINYINT(1) NOT NULL,
    PRIMARY KEY (userID)
);

-- Item
CREATE TABLE items (
    itemID INT AUTO_INCREMENT UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    allowedRentalDays INT NOT NULL,
    available TINYINT(1) NOT NULL,

    deleted TINYINT(1) NOT NULL,
    PRIMARY KEY (itemID)
);

-- Rental
CREATE TABLE rentals (
    rentalID INT AUTO_INCREMENT UNIQUE NOT NULL,
    userID INT NOT NULL,
    itemID INT NOT NULL,
    rentalDate DATETIME NOT NULL,
    rentalDueDate DATETIME NOT NULL,
    rentalReturnDate DATETIME,
    lateFee DOUBLE NOT NULL,

    deleted TINYINT(1) NOT NULL,
    PRIMARY KEY (rentalID),
    FOREIGN KEY (userID) REFERENCES users (userID),
    FOREIGN KEY (itemID) REFERENCES items (itemID)
);
-- JOIN TABLES ---------------------------------------------------------------------------------------------------------