-- 创建用户表（User）
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('user', 'admin')),
    contact_info VARCHAR(100)
);

-- 创建分类表（Category）
CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL
);

-- 创建图书表（Book）
CREATE TABLE book (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(50) NOT NULL,
    isbn VARCHAR(20),
    publish_date DATE,
    tag VARCHAR(50)
);

-- 图书类别表（BookCategory）
CREATE TABLE book_category (
    book_category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL ,
    book_id INT NOT NULL ,
    FOREIGN KEY (category_id) REFERENCES Category(category_id) ,
    FOREIGN KEY (book_id) REFERENCES book(book_id)  ON DELETE CASCADE
);
-- 创建留言表（Message）
CREATE TABLE message(
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    parent_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (parent_id) REFERENCES message(message_id)
);


-- 创建公告表（Announcement）
CREATE TABLE announcement (
    announcement_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE book_category
DROP FOREIGN KEY book_category_ibfk_1;
ALTER TABLE book_category
ADD CONSTRAINT book_category_ibfk_1
FOREIGN KEY (category_id) REFERENCES category (category_id)
ON DELETE CASCADE;

ALTER TABLE message
DROP FOREIGN KEY message_ibfk_1;

ALTER TABLE message
ADD CONSTRAINT message_ibfk_1
FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE;


