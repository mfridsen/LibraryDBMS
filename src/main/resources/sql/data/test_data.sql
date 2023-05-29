-- //TODO add Author test data
-- ('', '', '', ),
INSERT INTO authors (authorFirstname, authorLastName, biography, deleted) VALUES
('author1', 'lastname1', 'is the first author', 0),
('author2', 'lastname2', 'is the second author', 0),
('author3', 'lastname3', 'is the third author', 0),
('author4', 'lastname4', 'is the fourth author', 0),
('author5', 'lastname5', 'is the fifth author', 0),
('author6', 'lastname6', 'is the sixth author', 0),
('author7', 'lastname7', 'is the seventh author', 0),
('author8', 'lastname8', 'is the eighth author', 0),
('author9', 'lastname9', 'is the ninth author', 0),
('author10', 'lastname10', 'is the tenth author', 0);

-- //TODO add Classification test data
-- ('', '', ),
INSERT INTO classifications (classificationName, description, deleted) VALUES
('Physics', 'Scientific literature on the topic of physics', 0),
('Chemistry', 'Scientific literature on the topic of chemistry', 0),
('Mathematics', 'Scientific literature on the topic of mathematics', 0),
('Geography', 'Scientific literature on the topic of geography', 0),
('Geology', 'Scientific literature on the topic of geology', 0),
('Biology', 'Scientific literature on the topic of biology', 0),
('Programming', 'Scientific literature on the topic of programming', 0),
('Horror', 'The best genre', 0),
('Psychology', 'Scientific literature on the topic of psychology', 0),
('Fantasy', 'Tolkien did it best', 0);

-- //TODO add Publisher test data
-- ('', '', ),
INSERT INTO publishers (publisherName, email, deleted) VALUES
('publisher1', 'publisher1@email.com', 0),
('publisher2', 'publisher2@email.com', 0),
('publisher3', 'publisher3@email.com', 0),
('publisher4', 'publisher4@email.com', 0),
('publisher5', 'publisher5@email.com', 0),
('publisher6', 'publisher6@email.com', 0),
('publisher7', 'publisher7@email.com', 0),
('publisher8', 'publisher8@email.com', 0),
('publisher9', 'publisher9@email.com', 0),
('publisher10', 'publisher10@email.com', 0);

-- //TODO add Item test data here
INSERT INTO items (title, allowedRentalDays, available, deleted) VALUES
('item1', 14, 1, 0),
('item2', 14, 1, 0),
('item3', 14, 1, 0),
('item4', 14, 1, 0),
('item5', 14, 1, 0),
('item6', 14, 1, 0),
('item7', 14, 1, 0),
('item8', 14, 1, 0),
('item9', 14, 1, 0),
('item10', 14, 1, 0);
-- ('The Blade Itself', 14, 1, 0),
-- ('Before They Are Hanged', 14, 1, 0),
-- ('Last Argument Of Kings', 14, 1, 0),
-- ('Best Served Cold', 14, 1, 0),
-- ('The Heroes', 14, 1, 0),
-- ('Red Country', 14, 1, 0),
-- ('Sharp Ends', 14, 1, 0),
-- ('A Little Hatred', 14, 1, 0),
-- ('The Trouble With Peace', 14, 1, 0),
-- ('The Wisdom Of Crowds', 14, 1, 0);

-- //TODO Add user test data here
INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee, allowedToRent, deleted) VALUES
('user1', 'password1', 5, 0, 0.0, 1, 0),
('user2', 'password2', 5, 0, 0.0, 1, 0),
('user3', 'password3', 5, 0, 0.0, 1, 0),
('user4', 'password4', 5, 0, 0.0, 1, 0),
('user5', 'password5', 5, 0, 0.0, 1, 0),
('user6', 'password6', 5, 0, 0.0, 1, 0),
('user7', 'password7', 5, 0, 0.0, 1, 0),
('user8', 'password8', 5, 0, 0.0, 1, 0),
('user9', 'password9', 5, 0, 0.0, 1, 0),
('user10', 'password10', 5, 0, 0.0, 1, 0);

-- //TODO add Rental test data here
