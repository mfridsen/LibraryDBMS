-- (''),

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
('uname', 'password', 5, 0, 0.0, 1, 0);

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
('item10', 14, 1, 0),
('The Blade Itself', 14, 1, 0),
('Before They Are Hanged', 14, 1, 0),
('Last Argument Of Kings', 14, 1, 0),
('Best Served Cold', 14, 1, 0),
('The Heroes', 14, 1, 0),
('Red Country', 14, 1, 0),
('Sharp Ends', 14, 1, 0),
('A Little Hatred', 14, 1, 0),
('The Trouble With Peace', 14, 1, 0),
('The Wisdom Of Crowds', 14, 1, 0);

-- //TODO add Rental test data here
