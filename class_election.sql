CREATE DATABASE IF NOT EXISTS class_election;
USE class_election;
CREATE TABLE IF NOT EXISTS admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);
INSERT INTO admin (username, password) VALUES 
('Deena', 'pass123'),
('Maha', 'pass456'),
('Urooj', 'pass789'),
('Hassan', 'pass321');
CREATE TABLE IF NOT EXISTS candidate (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    registration_number VARCHAR(50) NOT NULL,
    description TEXT,
    election_type VARCHAR(50) NOT NULL
);
INSERT INTO candidate (name, registration_number, description, election_type) VALUES
('Ayesha ', 'CS-001', 'Hardworking and responsible', 'President'),
('Sara ', 'CS-002', 'Innovative leader with great ideas', 'President'),
('Hadia', 'CS-003', 'Great communication and teamwork', 'President'),
('Ali', ' CS-004', 'Committed to class improvement', 'CR'),
('Usman', 'CS-005', 'Creative and collaborative', 'CR'),
('Ahmad', 'CS-006', 'Friendly and organized', 'CR');
CREATE TABLE IF NOT EXISTS votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    voter_reg_no VARCHAR(50) NOT NULL,
    candidate_id INT NOT NULL,
    election_type VARCHAR(50) NOT NULL,
    UNIQUE (voter_reg_no, election_type), -- One vote per election per voter
    FOREIGN KEY (candidate_id) REFERENCES candidate(id) ON DELETE CASCADE
);
drop DATABASe class_election