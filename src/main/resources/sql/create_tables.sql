-- @author Mattias Fridsén
-- @project LibraryDBMS
-- @date 2/28/2023
-- @contact matfir-1@student.ltu.se

-- Creates all tables in the database

-- BASE TABLES ---------------------------------------------------------------------------------------------------------

-- Author, depended on by Item
CREATE TABLE `authors` (
    authorID INT AUTO_INCREMENT UNIQUE NOT NULL,
    authorFirstname VARCHAR(100) NOT NULL,
    authorLastName VARCHAR(100),
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

-- //TODO LOOK OVER
CREATE TABLE literature (
    literatureID INT PRIMARY KEY,
    ISBN VARCHAR(13) NOT NULL
);
-- //TODO LOOK OVER
CREATE TABLE films (
    filmID INT PRIMARY KEY,
    countryOfProduction VARCHAR(100),
    actors TEXT
);

-- Item, dependent on Author, Classification, depended on by Rental
CREATE TABLE items (
    itemID INT AUTO_INCREMENT UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    -- ENUM TYPE
    -- barcode
    -- authorID INT NOT NULL,
    -- classificationID INT NOT NULL,
    allowedRentalDays INT NOT NULL,
    available TINYINT(1) NOT NULL,
    deleted TINYINT(1) NOT NULL,
    PRIMARY KEY (itemID)
);

-- //TODO LOOK OVER
CREATE TABLE literature_item (
    literatureID INT,
    itemID INT,
    PRIMARY KEY (literatureID, itemID),
    FOREIGN KEY (literatureID) REFERENCES literature(literatureID),
    FOREIGN KEY (itemID) REFERENCES items(itemID)
);
-- //TODO LOOK OVER
CREATE TABLE film_item (
    filmID INT,
    itemID INT,
    PRIMARY KEY (filmID, itemID),
    FOREIGN KEY (filmID) REFERENCES films(filmID),
    FOREIGN KEY (itemID) REFERENCES items(itemID)
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
-- JOIN TABLES ---------------------------------------------------------------------------------------------------------