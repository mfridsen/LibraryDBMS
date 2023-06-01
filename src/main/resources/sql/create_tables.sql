-- @author Mattias Fridsén
-- @project LibraryDBMS
-- @date 2/28/2023
-- @contact matfir-1@student.ltu.se

-- Creates all tables in the database

-- Author, depended on by Item
CREATE TABLE `authors` (
    authorID INT AUTO_INCREMENT UNIQUE NOT NULL,
    authorFirstname VARCHAR(100) NOT NULL,
    authorLastname VARCHAR(100),
    biography TEXT,
    deleted TINYINT(1) NOT NULL,
    PRIMARY KEY (authorID)
);

-- Classification, depended on by Item
CREATE TABLE `classifications` (
    classificationID INT AUTO_INCREMENT UNIQUE NOT NULL,
    classificationName VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    deleted TINYINT(1) NOT NULL,
    PRIMARY KEY (classificationID)
);

-- Item, dependent on Author, Classification, depended on by Rental
CREATE TABLE items (
    itemID INT PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    itemType ENUM('REFERENCE_LITERATURE', 'MAGAZINE', 'FILM', 'COURSE_LITERATURE', 'OTHER_BOOKS'),
    barcode VARCHAR(255) UNIQUE NOT NULL,
    authorID INT NOT NULL,
    classificationID INT NOT NULL,
    allowedRentalDays INT NOT NULL,
    available TINYINT(1) NOT NULL,
    deleted TINYINT(1) NOT NULL,
    FOREIGN KEY (authorID) REFERENCES authors(authorID),
    FOREIGN KEY (classificationID) REFERENCES classifications(classificationID)
);

-- Literature
CREATE TABLE literature (
    literatureID INT PRIMARY KEY UNIQUE NOT NULL,
    ISBN VARCHAR(13) NOT NULL,
    FOREIGN KEY (literatureID) REFERENCES items(itemID)
);

-- Film
CREATE TABLE films (
    filmID INT PRIMARY KEY UNIQUE NOT NULL,
    ageRating INT NOT NULL,
    countryOfProduction VARCHAR(100),
    actors TEXT,
    FOREIGN KEY (filmID) REFERENCES items(itemID)
);

-- User, depended on by Rental
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

-- Rental, dependent on Item and User
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