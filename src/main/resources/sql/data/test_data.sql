-- (''),

-- //TODO Add user test data here
INSERT INTO users (username, passwordword, allowedRentals, currentRentals, lateFee) VALUES
('user1', 'password1', 5, 0, 0.0),
('user2', 'password2', 5, 0, 0.0),
('user3', 'password3', 5, 0, 0.0),
('user4', 'password4', 5, 0, 0.0),
('user5', 'password5', 5, 0, 0.0),
('user6', 'password6', 5, 0, 0.0),
('user7', 'password7', 5, 0, 0.0),
('user8', 'password8', 5, 0, 0.0),
('user9', 'password9', 5, 0, 0.0),
('uname', 'password', 5, 0, 0.0);

-- //TODO add Item test data here
INSERT INTO items (title, allowedRentalDays, available) VALUES
('item1', 14, 1),
('item2', 14, 1),
('item3', 14, 1),
('item4', 14, 1),
('item5', 14, 1),
('item6', 14, 1),
('item7', 14, 1),
('item8', 14, 1),
('item9', 14, 1),
('item10', 14, 1),
('The Blade Itself', 14, 1),
('Before They Are Hanged', 14, 1),
('Last Argument Of Kings', 14, 1),
('Best Served Cold', 14, 1),
('The Heroes', 14, 1),
('Red Country', 14, 1),
('Sharp Ends', 14, 1),
('A Little Hatred', 14, 1),
('The Trouble With Peace', 14, 1),
('The Wisdom Of Crowds', 14, 1);

-- //TODO add Rental test data here
