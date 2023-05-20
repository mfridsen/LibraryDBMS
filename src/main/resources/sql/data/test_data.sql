-- (''),

-- //TODO Add user test data here
INSERT INTO users (username, password, allowedRentals, currentRentals, lateFee) VALUES
('user1', 'pass1', 5, 0, 0.0),
('user2', 'pass2', 5, 0, 0.0),
('user3', 'pass3', 5, 0, 0.0),
('user4', 'pass4', 5, 0, 0.0),
('user5', 'pass5', 5, 0, 0.0),
('user6', 'pass6', 5, 0, 0.0),
('user7', 'pass7', 5, 0, 0.0),
('user8', 'pass8', 5, 0, 0.0),
('user9', 'pass9', 5, 0, 0.0),
('uname', 'pword', 5, 0, 0.0);

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
