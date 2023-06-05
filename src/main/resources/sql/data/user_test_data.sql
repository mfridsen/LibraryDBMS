INSERT INTO users (userID, username, password, userType, email, allowedRentals, currentRentals, lateFee, allowedToRent,
                   deleted)
VALUES (1, 'user1', 'password1', 'ADMIN', 'admin@example.com', 0, 0, 0.0, 0, 0),
       (2, 'user2', 'password2', 'STAFF', 'staff@example.com', 0, 0, 0.0, 0, 0),
       (3, 'user3', 'password3', 'PATRON', 'patron@example.com', 3, 0, 0.0, 1, 0),
       (4, 'user4', 'password4', 'STUDENT', 'student@example.com', 5, 0, 0.0, 1, 0),
       (5, 'user5', 'password5', 'TEACHER', 'teacher@example.com', 10, 0, 0.0, 1, 0),
       (6, 'user6', 'password6', 'RESEARCHER', 'researcher@example.com', 20, 0, 0.0, 1, 0),
       (7, 'user7', 'password7', 'ADMIN', 'admin2@example.com', 0, 0, 0.0, 0, 0),
       (8, 'user8', 'password8', 'PATRON', 'patron2@example.com', 3, 0, 0.0, 1, 0),
       (9, 'user9', 'password9', 'STAFF', 'staff2@example.com', 0, 0, 0.0, 0, 0),
       (10, 'user10', 'password10', 'STUDENT', 'student2@example.com', 5, 0, 0.0, 1, 0);
