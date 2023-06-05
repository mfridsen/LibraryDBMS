-- @author Mattias Fridsén
-- @project LibraryDBMS
-- @date 2/28/2023
-- @contact matfir-1@student.ltu.se

-- Creates all tables in the database

-- Author, depended on by Item
CREATE TABLE `authors`
(
    authorID        INT AUTO_INCREMENT UNIQUE NOT NULL,
    authorFirstname VARCHAR(100) NOT NULL,
    authorLastname  VARCHAR(100),
    biography       TEXT,
    deleted         TINYINT(1) NOT NULL,
    PRIMARY KEY (authorID)
);

-- Classification, depended on by Item
CREATE TABLE `classifications`
(
    classificationID   INT AUTO_INCREMENT UNIQUE NOT NULL,
    classificationName VARCHAR(255) UNIQUE NOT NULL,
    description        TEXT,
    deleted            TINYINT(1) NOT NULL,
    PRIMARY KEY (classificationID)
);

-- Item, dependent on Author, Classification, depended on by Rental
CREATE TABLE items
(
    itemID            INT PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
    title             VARCHAR(255)        NOT NULL,
    itemType          ENUM('REFERENCE_LITERATURE', 'MAGAZINE', 'FILM', 'COURSE_LITERATURE', 'OTHER_BOOKS'),
    barcode           VARCHAR(255) UNIQUE NOT NULL,
    authorID          INT                 NOT NULL,
    classificationID  INT                 NOT NULL,
    allowedRentalDays INT                 NOT NULL,
    available         TINYINT(1) NOT NULL,
    deleted           TINYINT(1) NOT NULL,
    FOREIGN KEY (authorID) REFERENCES authors (authorID),
    FOREIGN KEY (classificationID) REFERENCES classifications (classificationID)
);

-- Literature
CREATE TABLE literature
(
    literatureID INT PRIMARY KEY UNIQUE NOT NULL,
    ISBN         VARCHAR(13)            NOT NULL,
    FOREIGN KEY (literatureID) REFERENCES items (itemID)
);

-- Film
CREATE TABLE films
(
    filmID              INT PRIMARY KEY UNIQUE NOT NULL,
    ageRating           INT                    NOT NULL,
    countryOfProduction VARCHAR(100),
    actors              TEXT,
    FOREIGN KEY (filmID) REFERENCES items (itemID)
);

-- User, depended on by Rental
CREATE TABLE `users`
(
    userID         INT AUTO_INCREMENT UNIQUE NOT NULL,                                   -- Can't be < 0
    username       VARCHAR(20) UNIQUE  NOT NULL,                                         -- Can't be < 3, null or empty
    password       VARCHAR(50)         NOT NULL,                                         -- Can't be < 8, null or empty
    userType       ENUM('ADMIN', 'STAFF', 'PATRON', 'STUDENT', 'TEACHER', 'RESEARCHER'), -- Can't be null
    email          VARCHAR(255) UNIQUE NOT NULL,                                         -- Can't be < 6, null or empty
    allowedRentals INT                 NOT NULL,                                         -- Can't be < 0
    currentRentals INT                 NOT NULL,                                         -- Can't be < 0, or > allowedRentals
    lateFee        DOUBLE              NOT NULL,                                         -- Can't be < 0
    allowedToRent  TINYINT(1) NOT NULL,                                                  -- Rules found in User class
    deleted        TINYINT(1) NOT NULL,
    PRIMARY KEY (userID)
);

-- Rental, dependent on Item and User
CREATE TABLE rentals
(
    rentalID         INT AUTO_INCREMENT UNIQUE NOT NULL,
    userID           INT      NOT NULL,
    itemID           INT      NOT NULL,
    rentalDate       DATETIME NOT NULL,
    rentalDueDate    DATETIME NOT NULL,
    rentalReturnDate DATETIME,
    lateFee          DOUBLE   NOT NULL,
    receipt          TEXT     NOT NULL,
    deleted          TINYINT(1) NOT NULL,
    PRIMARY KEY (rentalID),
    FOREIGN KEY (userID) REFERENCES users (userID),
    FOREIGN KEY (itemID) REFERENCES items (itemID)
);