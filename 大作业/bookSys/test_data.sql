INSERT INTO user (username, password, role, contact_info) VALUES
('admin1', 'password123', 'admin', 'admin1@example.com'),
('user1', 'password456', 'user', 'user1@example.com'),
('user2', 'password789', 'user', 'user2@example.com');

INSERT INTO category (category_name) VALUES
('Fiction'),
('Non-Fiction'),
('Science'),
('Technology'),
('History');

INSERT INTO book (title, author, isbn, publish_date, tag) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', '1925-04-10', 'Classic'),
('A Brief History of Time', 'Stephen Hawking', '9780553380163', '1988-06-15', 'Physics'),
('Clean Code', 'Robert C. Martin', '9780132350884', '2008-08-01', 'Programming'),
('Sapiens', 'Yuval Noah Harari', '9780062316097', '2011-01-01', 'History'),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', '1960-07-11', 'Classic');

INSERT INTO book_category (category_id, book_id) VALUES
(1, 1), -- Fiction: The Great Gatsby
(3, 2), -- Science: A Brief History of Time
(4, 3), -- Technology: Clean Code
(5, 4), -- History: Sapiens
(1, 5); -- Fiction: To Kill a Mockingbird

INSERT INTO message (book_id, user_id, content, parent_id) VALUES
(1, 2, 'This book is a masterpiece!', NULL), -- User1 about The Great Gatsby
(1, 3, 'I agree, a classic for sure.', NULL), -- User2 reply to User1
(3, 2, 'Very helpful for learning clean coding practices.', NULL); -- User1 about Clean Code

INSERT INTO announcement (title, content) VALUES
('Library Maintenance Notice', 'The library system will be under maintenance on Dec 10 from 12 AM to 6 AM.'),
('New Arrivals', 'We have added new books to the Fiction and Science categories. Check them out!');
