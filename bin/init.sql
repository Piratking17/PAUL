
CREATE DATABASE IF NOT EXISTS notetakingdb;

USE notetakingdb;

CREATE TABLE IF NOT EXISTS notes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT
);
