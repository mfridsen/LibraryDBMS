-- @author Mattias Fridsén
-- @project DatabaseApplicationTemplate
-- @date 2/28/2023
-- @contact matfir-1@student.ltu.se

-- Creates all tables in the database

-- BASE TABLES ---------------------------------------------------------------------------------------------------------

-- User
CREATE TABLE `User` (
    user_ID INT AUTO_INCREMENT UNIQUE NOT NULL,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_ID)
);

--Item
CREATE TABLE Item (
    item_ID INT AUTO_INCREMENT UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    PRIMARY KEY (item_ID)
);

--Checkout
CREATE TABLE Checkout (
    checkout_ID INT AUTO_INCREMENT UNIQUE NOT NULL,
    user_ID INT NOT NULL,
    item_ID INT NOT NULL,
    PRIMARY KEY (checkout_ID),
    FOREIGN KEY (user_ID) REFERENCES user(user_ID),
    FOREIGN KEY (item_ID) REFERENCES item(item_ID)
);
-- JOIN TABLES ---------------------------------------------------------------------------------------------------------