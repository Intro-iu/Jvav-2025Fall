DROP DATABASE IF EXISTS news_db;
CREATE DATABASE news_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE news_db;

-- Users table (Editors/Admins)
CREATE TABLE t_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL, -- In a real app, use hashing!
    nickname VARCHAR(50)
);

-- Categories table
CREATE TABLE t_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- News table
CREATE TABLE t_news (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    category_id INT,
    author_id INT,
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES t_category(id),
    FOREIGN KEY (author_id) REFERENCES t_user(id)
);

-- Insert default admin user
INSERT INTO t_user (username, password, nickname) VALUES ('admin', 'admin123', 'Administrator');

-- Insert default categories
INSERT INTO t_category (name) VALUES ('Technology'), ('Sports'), ('Politics'), ('Entertainment');
