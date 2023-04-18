-- @author Mattias Fridsén
-- @project DatabaseApplicationTemplate
-- @date 2/28/2023
-- @contact matfir-1@student.ltu.se

-- Creates all tables in the database

-- BASE TABLES ---------------------------------------------------------------------------------------------------------

-- User
CREATE TABLE `User` (
    `userID` INT AUTO_INCREMENT UNIQUE NOT NULL,
    `username` VARCHAR(20) UNIQUE NOT NULL,
    `password` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`userID`)
);

-- JOIN TABLES ---------------------------------------------------------------------------------------------------------